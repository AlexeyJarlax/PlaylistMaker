package com.practicum.playlistmaker

// Памятка о содержании файла:
//SearchActivity - активити и вся обработка поискового запроса юзера.
//UtilTrackViewHolder - холдер для RecyclerView, отображающий информацию о треках.
// AdapterForAPITracks - адаптер для RecyclerView, отображающий информацию о треках.
//iTunesApiService - интерфейс для iTunes Search API.
//TrackResponse - класс данных, представляющий ответ от iTunes Search API.
//ITunesTrack - класс данных для преобразования ответа iTunes Search API в список объектов TrackData.
//TrackData - класс данных, представляющий список треков на устройстве.
//OnTrackItemClickListener - интерфейс для обработки истории

// Этапы поиска:
//1. этап: считываем ввод в queryInput.setOnEditorActionListener и queryInput.addTextChangedListener ===> запуск 2 этапа
//2. этап: передаем searchText в fun preparingForSearch для активации loadingIndicator и блокировки кнопок ===> запуск 3 этапа
//3. этап: передаем searchText в fun performSearch => вызываем TrackResponse => заполняем TrackData  ===> вывод списка песен, соответствующих запросу
//3.1 : performSearch => [возникла ошибка с вызовом TrackResponse] => Запускаем метод solvingConnectionProblem() ===> Запускаем повторно 2 этап

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var adapterForAPITracks: AdapterForAPITracks
    private val cleanTrackList = ArrayList<TrackData>()
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
        loadingIndicator = findViewById(R.id.loading_indicator)
        loadingIndicator.visibility = View.GONE
        utilErrorBox = findViewById<LinearLayout>(R.id.util_error_box)
        searchHistoryNotification = findViewById(R.id.you_were_looking_for)
        killTheHistory = findViewById(R.id.kill_the_history)
    }

    private fun callAdapterForHistoryTracks() {
        adapterForHistoryTracks = AdapterForHistoryTracks(this, object : OnTrackItemClickListener {
            override fun onTrackItemClick(
                trackName: String?,
                artistName: String?,
                trackTimeMillis: Long?,
                artworkUrl100: String?,
                collectionName: String?,
                releaseDate: String?,
                primaryGenreName: String?,
                country: String?
            ) {
                // повторный клик на треке в истории треков
                adapterForHistoryTracks.saveTrack(
                    trackName,
                    artistName,
                    trackTimeMillis,
                    artworkUrl100,
                    collectionName,
                    releaseDate,
                    primaryGenreName,
                    country
                )
            }
        })
        adapterForHistoryTracks.setRecyclerView(trackRecyclerView)
    }

    private fun setupRecyclerViewAndAdapter() {
        val layoutManager = LinearLayoutManager(this)
        adapterForAPITracks =
            AdapterForAPITracks(this, cleanTrackList, object : OnTrackItemClickListener {
                override fun onTrackItemClick(
                    trackName: String?,
                    artistName: String?,
                    trackTimeMillis: Long?,
                    artworkUrl100: String?,
                    collectionName: String?,
                    releaseDate: String?,
                    primaryGenreName: String?,
                    country: String?
                ) {
                    adapterForHistoryTracks.saveTrack(
                        trackName,
                        artistName,
                        trackTimeMillis,
                        artworkUrl100,
                        collectionName,
                        releaseDate,
                        primaryGenreName,
                        country
                    )
//                    toastIt("${getString(R.string.added)} ${trackName}")
                    Timber.d("historyAdapter.saveTrack:${trackName}${artistName}")
                }
            })
        trackRecyclerView.layoutManager = layoutManager
        trackRecyclerView.adapter = adapterForAPITracks
    }

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
                    hideHistoryViewsAndClearTrackAdapter()
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
//        trackAdapter.updateList(cleanTrackList)
//        trackAdapter.clearList()
    }

    private fun killTheHistory() {
        killTheHistory.setOnClickListener {
            adapterForHistoryTracks.killHistoryList()
            hideHistoryViewsAndClearTrackAdapter()
        }
    }

    private fun queryInputListener() { // обработка ввода с нажатием DONE
        queryInput.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val searchText = queryInput.text.toString().trim()
                if (searchText.isNotEmpty()) {
                    utilErrorBox.visibility = View.GONE
                    clearTrackAdapter()
                    preparingForSearch(searchText)
                    toastIt("${getString(R.string.search)} $searchText")
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

    private fun preparingForSearch(searchText: String) {
        loadingIndicator.visibility = View.VISIBLE
        clearButton.isEnabled = false
        Handler(Looper.getMainLooper()).postDelayed({
            performSearch(searchText) { trackItems ->
                loadingIndicator.visibility = View.GONE
                clearButton.isEnabled = true
                adapterForAPITracks.updateList(trackItems)
                adapterForAPITracks.setRecyclerView(trackRecyclerView)
                trackRecyclerView.visibility = View.VISIBLE
            }
        }, AppPreferencesKeys.SERVER_PROCESSING_MILLISECONDS)
    }

    private var lastQuery: String? = null
    private var lastCallback: ((List<TrackData>) -> Unit)? = null
    private fun performSearch(query: String, callback: (List<TrackData>) -> Unit) {
        lastQuery = query        // Сохраняем последний запрос и колбэк
        lastCallback = callback
        Timber.d("Запускаем метод performSearch с параметрами Query: $query и Callback")
        iTunesSearchAPI.search(query).enqueue(object : Callback<TrackResponse> {
            override fun onResponse(
                call: Call<TrackResponse>, response: Response<TrackResponse>
            ) {
                if (response.code() == 200) {
                    val trackResponse = response.body()
                    val trackData = if (trackResponse?.results?.isNotEmpty() == true) {
                        // Преобразуем результаты в список объектов TrackData
                        trackResponse.results.map { track ->
                            Timber.d("Метод performSearch => response.isSuccessful! track.trackName:${track.trackName}")
                            val releaseDateTime = LocalDateTime.parse(track.releaseDate, DateTimeFormatter.ISO_DATE_TIME)
                            TrackData(
                                track.trackName ?: "",
                                track.artistName ?: "",
                                track.trackTimeMillis ?: 0,
                                track.artworkUrl100 ?: "",
                                track.collectionName ?: "",
                                releaseDateTime.year.toString(),
                                track.primaryGenreName ?: "",
                                track.country ?: ""
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
                        400 -> "${getString(R.string.error400)}"
                        401 -> "${getString(R.string.error401)}"
                        403 -> "${getString(R.string.error403)}"
                        404 -> "${getString(R.string.error404)}"
                        500 -> "${getString(R.string.error500)}"
                        503 -> "${getString(R.string.error503)}"
                        else -> "${getString(R.string.error0)}"
                    }
                    Timber.d(error)
                    toastIt(error)
                    onFailure(
                        call, Throwable(error)
                    )
                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                solvingConnectionProblem()
                val trackData = emptyList<TrackData>()
                callback(trackData)
            }
        })
    }

    private fun solvingAbsentProblem() {
        loadingIndicator.visibility = View.GONE
        val errorIcon = findViewById<ImageView>(R.id.error_icon)
        val errorTextWeb = findViewById<TextView>(R.id.error_text_web)
        errorIcon.setImageResource(R.drawable.ic_error_notfound)
        errorTextWeb.text = resources.getString(R.string.nothing_was_found)
        val retryButton = findViewById<Button>(R.id.retry_button)
        retryButton.visibility = View.GONE // тут кнопка не нужна
        utilErrorBox.visibility = View.VISIBLE
        utilErrorBox.setOnClickListener {
            utilErrorBox.visibility = View.GONE
        }
    }

    private fun solvingConnectionProblem() {
        loadingIndicator.visibility = View.GONE
        val errorIcon = findViewById<ImageView>(R.id.error_icon)
        val errorTextWeb = findViewById<TextView>(R.id.error_text_web)
        errorIcon.setImageResource(R.drawable.ic_error_internet)
        errorTextWeb.text = resources.getString(R.string.error_text_web)
        val retryButton = findViewById<Button>(R.id.retry_button)
        retryButton.visibility = View.VISIBLE
        utilErrorBox.visibility = View.VISIBLE

        retryButton.setOnClickListener {
            lastQuery?.let { query ->
                lastCallback?.let { callback ->
                    preparingForSearch(query)
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
        clearButton.setOnClickListener {
            queryInput.text.clear()
        }
    }

    private fun backToMain() {
        backButton.setOnClickListener {
            finish()
        }
    }

    private fun toastIt(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

class UtilTrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackNameTextView: TextView = itemView.findViewById(R.id.track_name_text_view)
    private val artistNameTextView: TextView = itemView.findViewById(R.id.artist_name_text_view)
    private val trackTimeTextView: TextView = itemView.findViewById(R.id.track_duration_text_view)
    private val artworkImageView: ImageView = itemView.findViewById(R.id.artwork_image_view)
    private val playButton: LinearLayout = itemView.findViewById(R.id.util_item_track)

    fun bind(trackData: TrackData, trackItemClickListener: OnTrackItemClickListener) {
        trackNameTextView.text = trackData.trackName ?: ""
        artistNameTextView.text = trackData.artistName ?: ""
        trackTimeTextView.text = trackData.trackTimeMillis?.let { formatTrackDuration(it) } ?: ""
        trackData.artworkUrl100?.let { loadImage(it, artworkImageView) }

        playButton.setOnClickListener {
            trackItemClickListener.onTrackItemClick(
                trackData.trackName ?: "",
                trackData.artistName ?: "",
                trackData.trackTimeMillis ?: 0,
                trackData.artworkUrl100 ?: "",
                trackData.collectionName ?: "",
                trackData.releaseDate ?: "",
                trackData.primaryGenreName ?: "",
                trackData.country ?: ""
            )
            //Intent для перехода в окно проигрывателя PlayActivity
            val intent = Intent(itemView.context, PlayActivity::class.java)
            intent.putExtra("trackName", trackData.trackName)
            intent.putExtra("artistName", trackData.artistName)
            intent.putExtra("trackTimeMillis", trackData.trackTimeMillis)
            intent.putExtra("artworkUrl100", trackData.artworkUrl100)
            intent.putExtra("collectionName", trackData.collectionName)
            intent.putExtra("releaseDate", trackData.releaseDate)
            intent.putExtra("primaryGenreName", trackData.primaryGenreName)
            intent.putExtra("country", trackData.country)
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
    private var trackData: List<TrackData>,
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

    fun updateList(newList: List<TrackData>) {
        trackData = newList
        notifyDataSetChanged()
    }

    fun clearList() {
        val newList: MutableList<TrackData> = mutableListOf()
        trackData = newList
        notifyDataSetChanged()
    }
}

data class TrackData(
    val trackName: String?,        // Название
    val artistName: String?,       // Исполнитель
    val trackTimeMillis: Long?,    // Продолжительность
    val artworkUrl100: String?,    // Пикча на обложку
    val collectionName: String?,  // Название альбома
    val releaseDate: String?,     // Год
    val primaryGenreName: String?,// Жанр
    val country: String?          // Страна
)

data class ITunesTrack(
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: Long?,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?
)

data class TrackResponse(val results: List<ITunesTrack>)

interface OnTrackItemClickListener {
    fun onTrackItemClick(
        trackName: String?,
        artistName: String?,
        trackTimeMillis: Long?,
        artworkUrl100: String?,
        collectionName: String?,
        releaseDate: String?,
        primaryGenreName: String?,
        country: String?
    )
}

