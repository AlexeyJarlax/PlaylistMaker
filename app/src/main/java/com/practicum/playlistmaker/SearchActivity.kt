package com.practicum.playlistmaker

/* === Памятка о содержании файла:
SearchActivity - активити и вся обработка поискового запроса юзера.
UtilTrackViewHolder - холдер для RecyclerView, отображающий информацию о треках.
 AdapterForAPITracks - адаптер для RecyclerView, отображающий информацию о треках.
iTunesApiService - интерфейс для iTunes Search API.
TrackResponse - класс данных, представляющий ответ от iTunes Search API.
ITunesTrack - класс данных для преобразования ответа iTunes Search API в список объектов TrackData.
OnTrackItemClickListener - интерфейс для обработки истории
Track@Serializable - класс моделью данных, представляющей информацию о музыкальном треке одним объектом c возможностью упаковываться в джейсончики

=== Этапы поиска:
1. этап: считываем поле поиска
1.1 ввод в queryInput.setOnEditorActionListener и кнопка DONE  ===> запуск 2 этапа
1.2 ввод в queryInput.addTextChangedListener  ===> запуск 2 этапа через 2 секунды
2. этап: передаем searchText в searchStep1 => searchStep2, для активации progressBar ===> запуск 3 этапа
3. этап: передаем searchText в searchStep3 => вызываем TrackResponse => заполняем Track  ===> вывод списка песен, соответствующих запросу
3.1 : performSearch => [возникла ошибка с вызовом TrackResponse] => Запускаем метод solvingConnectionProblem() ===> Запускаем повторно 2 этап

=== Объект track содержит:
val trackName: String?          // Название
val artistName: String?         // Исполнитель
val trackTimeMillis: Long?      // Продолжительность
val artworkUrl100: String?      // Пикча на обложку
val collectionName: String?     // Название альбома
val releaseDate: String?        // Год
val primaryGenreName: String?   // Жанр
val country: String?            // Страна
val previewUrl: String?         // ссылка на 30 сек. фрагмент
*/
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.util.AdapterForHistoryTracks
import com.practicum.playlistmaker.util.AppPreferencesKeys
import com.practicum.playlistmaker.util.DebounceExtension
import com.practicum.playlistmaker.util.openThread
import com.practicum.playlistmaker.util.setDebouncedClickListener
import com.practicum.playlistmaker.util.stopLoadingIndicator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.time.format.DateTimeParseException

class SearchActivity : AppCompatActivity() {

    private val iTunesSearch = "https://itunes.apple.com"
    private val retrofit =
        Retrofit.Builder().baseUrl(iTunesSearch).addConverterFactory(GsonConverterFactory.create())
            .build()
    private val iTunesSearchAPI = retrofit.create(iTunesApiService::class.java)
    private var hasFocus = true
    private lateinit var queryInput: EditText
    private lateinit var clearButton: ImageButton
    private lateinit var backButton: Button
    private lateinit var trackRecyclerView: RecyclerView

    //    private lateinit var loadingIndicator: ProgressBar
    private lateinit var adapterForAPITracks: AdapterForAPITracks
    private val cleanTrackList = ArrayList<Track>()
    private lateinit var utilErrorBox: View
    private lateinit var adapterForHistoryTracks: AdapterForHistoryTracks
    private lateinit var searchHistoryNotification: TextView
    private lateinit var killTheHistory: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.plant(Timber.DebugTree()) // для логирования ошибок
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setupOneLineViews()
        clearButton()
        backToMain()
        callAdapterForHistoryTracks()
        setupRecyclerViewAndAdapter()
        queryTextChangedListener()
        queryInputListener()
        fillTrackAdapter()
        killTheHistory()
    }

    private fun setupOneLineViews() {
        backButton = findViewById(R.id.button_back_from_search_activity)
        clearButton = findViewById(R.id.clearButton)
        queryInput = findViewById(R.id.search_edit_text)
        trackRecyclerView = findViewById(R.id.track_recycler_view)
        utilErrorBox = findViewById<LinearLayout>(R.id.util_error_box)
        searchHistoryNotification = findViewById(R.id.you_were_looking_for)
        killTheHistory = findViewById(R.id.kill_the_history)
    }

    private fun callAdapterForHistoryTracks() {
        adapterForHistoryTracks = AdapterForHistoryTracks(this, object : OnTrackItemClickListener {
            override fun onTrackItemClick(track: Track) {
                adapterForHistoryTracks.saveTrack(track)
            }
        })
        adapterForHistoryTracks.setRecyclerView(trackRecyclerView)
    }

    private fun setupRecyclerViewAndAdapter() {
        val layoutManager = LinearLayoutManager(this)
        adapterForAPITracks =
            AdapterForAPITracks(this, cleanTrackList, object : OnTrackItemClickListener {
                override fun onTrackItemClick(track: Track) {
                    adapterForHistoryTracks.saveTrack(track)
                    Timber.d("historyAdapter.saveTrack:${track.trackName}${track.artistName}")
                }
            })
        trackRecyclerView.layoutManager = layoutManager
        trackRecyclerView.adapter = adapterForAPITracks
    }

//************************************************************ ввод в поле поиска и обработка ввода
    private fun queryTextChangedListener() {
        queryInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                val searchText = queryInput.text.toString().trim()
                clearButton.visibility = if (searchText.isNotEmpty()) View.VISIBLE else View.GONE
                if (hasFocus && searchText.isEmpty()) {  // обработка ввода без нажатий
                    showHistoryViewsAndFillTrackAdapter()
                } else {
                    searchWhisDebounce() // пытаемся искать песни во время паузы 2 секунды ввода
                }
            }
            override fun afterTextChanged(editable: Editable?) {
            }
        })
        // Фокус + ЖЦ вход в приложение queryInput пуст
        queryInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && queryInput.text.isEmpty()) {
                showHistoryViewsAndFillTrackAdapter()
            } else if (queryInput.text.isNotEmpty()) {
                hideHistoryViewsAndClearTrackAdapter()
            }
        }
    }

    // задержка в 2 сек для поиска во время ввода
    private val twoSecondDebounceSearch = DebounceExtension(AppPreferencesKeys.SEARCH_DEBOUNCE_DELAY) {
        searchStep1Preparation(queryInput.text.toString().trim())
    }

    private fun searchWhisDebounce() {
        hideHistoryViewsAndClearTrackAdapter()
        twoSecondDebounceSearch.debounce()
    }

    private fun showHistoryViewsAndFillTrackAdapter() {
        if (adapterForHistoryTracks.checkIfHistoryListExists()) {
            fillTrackAdapter()
            trackRecyclerView.visibility = View.VISIBLE
            searchHistoryNotification.visibility = View.VISIBLE
            killTheHistory.visibility = View.VISIBLE
        }
    }

    private fun fillTrackAdapter() {
        clearTrackAdapter()
        adapterForHistoryTracks?.setRecyclerView(trackRecyclerView)
        adapterForHistoryTracks?.syncTracks()
        trackRecyclerView.scrollToPosition(0)
    }

    private fun hideHistoryViewsAndClearTrackAdapter() {
        clearTrackAdapter()
        trackRecyclerView.visibility = View.GONE
        searchHistoryNotification.visibility = View.GONE
        killTheHistory.visibility = View.GONE
    }

    private fun clearTrackAdapter() {
        adapterForHistoryTracks.clearHistoryList() // чистит адаптер с историей
    }

    private fun killTheHistory() {
        killTheHistory.setDebouncedClickListener {
            adapterForHistoryTracks.killHistoryList()
            hideHistoryViewsAndClearTrackAdapter()
        }
    }

    private fun queryInputListener() { // обработка ввода с нажатием DONE
        queryInput.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val searchText = queryInput.text.toString().trim()
                if (searchText.isNotEmpty()) {
                    searchStep1Preparation(searchText)
                }
                hideKeyboard()
                true
            } else {
                false
            }
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(queryInput.windowToken, 0)
    }
    
    private fun searchStep1Preparation(searchText: String) {
    utilErrorBox.visibility = View.GONE
    clearTrackAdapter()
    searchStep2Thread(searchText)
    }
    
    private fun searchStep2Thread(searchText: String) {
        openThread {
            Timber.d("===preparingForSearch начинаем в потоке: ${Thread.currentThread().name}")
            searchStep3iTunesAPI(searchText) { trackItems ->
                Timber.d("=== performSearch в потоке: ${Thread.currentThread().name}")
                adapterForAPITracks.updateList(trackItems)
                runOnUiThread {
                    adapterForAPITracks.setRecyclerView(trackRecyclerView)
                    trackRecyclerView.visibility = View.VISIBLE
                    Timber.d("=== adapter и Recycler в потоке: ${Thread.currentThread().name}")
                    stopLoadingIndicator()
                }
            }
        }
    }

//************************************************************************************** iTunes API
    private var lastQuery: String? = null
    private var lastCallback: ((List<Track>) -> Unit)? = null

    private fun searchStep3iTunesAPI(query: String, callback: (List<Track>) -> Unit) {
        lastQuery = query        // Сохраняем последний запрос и колбэк
        lastCallback = callback
        Timber.d("Запускаем метод performSearch с параметрами Query: $query и Callback")

        iTunesSearchAPI.search(query).enqueue(object : Callback<TrackResponse> {
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                if (response.isSuccessful) {
                    val trackResponse = response.body()
                    val trackData = if (trackResponse?.results?.isNotEmpty() == true) {
                        // Преобразуем результаты в список объектов TrackData
                        trackResponse.results.map { track ->
                            Timber.d("Метод performSearch => response.isSuccessful! track.trackName:${track.trackName}")
                            val releaseDateTime = try {
                                track.releaseDate?.let {
                                    LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME)
                                } ?: LocalDateTime.MIN // Если releaseDate == null, используем минимальное значение LocalDateTime
                            } catch (e: DateTimeParseException) {
                                Timber.e(e, "Ошибка при разборе даты: ${track.releaseDate}")
                                LocalDateTime.MIN
                            }

                            Track(
                                track.trackName ?: "",
                                track.artistName ?: "",
                                track.trackTimeMillis ?: 0,
                                track.artworkUrl100 ?: "",
                                track.collectionName ?: "",
                                releaseDateTime.year.toString(),
                                track.primaryGenreName ?: "",
                                track.country ?: "",
                                track.previewUrl ?: ""
                            )
                        }
                    } else {
                        Timber.d("Метод performSearch => response.isSuccessful! => emptyList() таких песен нет")
                        solvingAbsentProblem() // вызываем заглушку о пустом листе запроса
                        emptyList()
                    }

                    callback(trackData)         // Вызываем колбэк с полученными данными
                    Timber.d("Метод performSearch => response.isSuccessful! => callback(trackData): $trackData")
                } else {
                    val error = when (response.code()) {
                        400 -> getString(R.string.error400)
                        401 -> getString(R.string.error401)
                        403 -> getString(R.string.error403)
                        404 -> getString(R.string.error404)
                        500 -> getString(R.string.error500)
                        503 -> getString(R.string.error503)
                        else -> getString(R.string.error0)
                    }
                    Timber.d(error)
                    toastIt(error)
                    onFailure(call, Throwable(error))
                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                solvingConnectionProblem()
                val trackData = emptyList<Track>()
                callback(trackData)
            }
        })
    }

    private fun solvingAbsentProblem() {
//        loadingIndicator.visibility = View.GONE
        val errorIcon = findViewById<ImageView>(R.id.error_icon)
        val errorTextWeb = findViewById<TextView>(R.id.error_text_web)
        errorIcon.setImageResource(R.drawable.ic_error_notfound)
        errorTextWeb.text = resources.getString(R.string.nothing_was_found)
        val retryButton = findViewById<Button>(R.id.retry_button)
        retryButton.visibility = View.GONE // тут кнопка не нужна
        utilErrorBox.visibility = View.VISIBLE
        utilErrorBox.setDebouncedClickListener {
            utilErrorBox.visibility = View.GONE
        }
    }

    private fun solvingConnectionProblem() {
//        loadingIndicator.visibility = View.GONE
        val errorIcon = findViewById<ImageView>(R.id.error_icon)
        val errorTextWeb = findViewById<TextView>(R.id.error_text_web)
        errorIcon.setImageResource(R.drawable.ic_error_internet)
        errorTextWeb.text = resources.getString(R.string.error_text_web)
        val retryButton = findViewById<Button>(R.id.retry_button)
        retryButton.visibility = View.VISIBLE
        utilErrorBox.visibility = View.VISIBLE

        retryButton.setDebouncedClickListener {
            lastQuery?.let { query ->
                lastCallback?.let { callback ->
                    searchStep2Thread(query)
                }
            }
            utilErrorBox.visibility = View.GONE
        }
    }

    interface iTunesApiService {
        @GET("search?entity=song")
        fun search(@Query("term") text: String): Call<TrackResponse>
    }

    private fun clearButton() {
        clearButton.setDebouncedClickListener {
            queryInput.text.clear()
        }
    }

    private fun backToMain() {
        backButton.setDebouncedClickListener {
                finish()
        }
    }

    private fun toastIt(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

//******************************************************************************* Adapter и Recycler
class UtilTrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackNameTextView: TextView = itemView.findViewById(R.id.track_name_text_view)
    private val artistNameTextView: TextView = itemView.findViewById(R.id.artist_name_text_view)
    private val trackTimeTextView: TextView = itemView.findViewById(R.id.track_duration_text_view)
    private val artworkImageView: ImageView = itemView.findViewById(R.id.artwork_image_view)
    private val playButton: LinearLayout = itemView.findViewById(R.id.util_item_track)

    fun bind(track: Track, trackItemClickListener: OnTrackItemClickListener) {
        trackNameTextView.text = track.trackName ?: ""
        artistNameTextView.text = track.artistName ?: ""
        trackTimeTextView.text = track.trackTimeMillis?.let { formatTrackDuration(it) } ?: ""
        track.artworkUrl100?.let { loadImage(it, artworkImageView) }

        playButton.setDebouncedClickListener {
            trackItemClickListener.onTrackItemClick(track)
            val intent = Intent(itemView.context, PlayActivity::class.java)
            val trackJson = Json.encodeToString(Track.serializer(), track)
            intent.putExtra("trackJson", trackJson)
            itemView.context.startActivity(intent)
        }
    }

    private fun formatTrackDuration(trackTimeMillis: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(trackTimeMillis)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(trackTimeMillis) - TimeUnit.MINUTES.toSeconds(
            minutes
        )
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun loadImage(imageUrl: String, imageView: ImageView) {
        Glide.with(imageView).load(imageUrl).placeholder(R.drawable.ic_placeholder)
            .transform(RoundedCorners(AppPreferencesKeys.ALBUM_ROUNDED_CORNERS))
            .error(R.drawable.ic_placeholder)
            .into(imageView)
    }
}

class AdapterForAPITracks(
    private val context: Context,
    private var trackData: List<Track>,
    private val trackItemClickListener: OnTrackItemClickListener
) : RecyclerView.Adapter<UtilTrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UtilTrackViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.util_item_track, parent, false)
        return UtilTrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: UtilTrackViewHolder, position: Int) {
        holder.bind(trackData[position], trackItemClickListener)
    }

    override fun getItemCount(): Int {
        return trackData.size
    }

    fun setRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = this
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    fun updateList(newList: List<Track>) {
        trackData = newList
        notifyDataSetChanged()
    }

    fun clearList() {
        val newList: MutableList<Track> = mutableListOf()
        trackData = newList
        notifyDataSetChanged()
    }
}

data class ITunesTrack(
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: Long?,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?
)

data class TrackResponse(val results: List<ITunesTrack>)

interface OnTrackItemClickListener {
    fun onTrackItemClick(track: Track)
}

@Serializable
data class Track(
    @SerialName("trackName") val trackName: String?,
    @SerialName("artistName") val artistName: String?,
    @SerialName("trackTimeMillis") val trackTimeMillis: Long?,
    @SerialName("artworkUrl100") val artworkUrl100: String?,
    @SerialName("collectionName") val collectionName: String?,
    @SerialName("releaseDate") val releaseDate: String?,
    @SerialName("primaryGenreName") val primaryGenreName: String?,
    @SerialName("country") val country: String?,
    @SerialName("previewUrl") val previewUrl: String?
) {
    fun toTrackData() = Track(
        trackName,
        artistName,
        trackTimeMillis,
        artworkUrl100,
        collectionName,
        releaseDate,
        primaryGenreName,
        country,
        previewUrl
    )
}

