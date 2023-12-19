package com.practicum.playlistmaker

// Памятка о содержании файла:
//SearchActivity - активити и вся обработка поискового запроса юзера.
//UtilTrackViewHolder - холдер для RecyclerView, отображающий информацию о треках.
//UtilTrackAdapter - адаптер для RecyclerView, отображающий информацию о треках.
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
import com.practicum.playlistmaker.util.UtilHistoryAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import timber.log.Timber
import java.util.concurrent.TimeUnit

class SearchActivity : AppCompatActivity() {

    private val iTunesSearch = "https://itunes.apple.com"
    private val retrofit =
        Retrofit.Builder().baseUrl(iTunesSearch).addConverterFactory(GsonConverterFactory.create())
            .build()
    private val iTunesSearchAPI = retrofit.create(iTunesApiService::class.java)
    private lateinit var queryInput: EditText
    private lateinit var clearButton: ImageButton
    private lateinit var backButton: Button
    private lateinit var trackRecyclerView: RecyclerView
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var trackAdapter: UtilTrackAdapter
    private val cleanTrackList = ArrayList<TrackData>()
    private lateinit var utilErrorBox: View
    private lateinit var utilHistoryAdapter: UtilHistoryAdapter // Добавим переменную
    var counter = 0 // счетчик сбросов

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.plant(Timber.DebugTree()) // для логирования ошибок
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setupOneLineViews()
        clearButton()
        backToMain()
//        setupHistoryAdapter()
        historyAdapter()
        setupTrackRecyclerViewAndTrackAdapter()
        queryInputListener() // заполнение с виртуальной клавиатуры
        queryTextChangedListener()
//        onFocusChangeListener(queryInput)
    }

    private fun setupOneLineViews() {
        backButton = findViewById<Button>(R.id.button_back_from_search_activity)
        clearButton = findViewById(R.id.clearButton)
        queryInput = findViewById(R.id.search_edit_text)
        trackRecyclerView = findViewById(R.id.track_recycler_view)
        loadingIndicator = findViewById(R.id.loading_indicator)
        loadingIndicator.visibility = View.GONE
        utilErrorBox = findViewById<LinearLayout>(R.id.util_error_box)
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

    private fun historyAdapter() {
        val trackAdapter =
            UtilTrackAdapter(this, mutableListOf(), object : OnTrackItemClickListener {
                override fun onTrackItemClick(
                    trackName: String,
                    artistName: String,
                    trackTimeMillis: Long,
                    artworkUrl100: String
                ) {
                    // Действия при клике на элемент истории, если нужны
                }
            })

        utilHistoryAdapter =
            UtilHistoryAdapter(this, trackAdapter, object : OnTrackItemClickListener {
                override fun onTrackItemClick(
                    trackName: String,
                    artistName: String,
                    trackTimeMillis: Long,
                    artworkUrl100: String
                ) {
                    // Действия при клике на элемент истории, если нужны
                }
            })
        utilHistoryAdapter.setRecyclerView(trackRecyclerView)
    }

    private fun setupTrackRecyclerViewAndTrackAdapter() {
        val layoutManager = LinearLayoutManager(this)
        trackAdapter = UtilTrackAdapter(this, cleanTrackList, object : OnTrackItemClickListener {
            override fun onTrackItemClick(
                trackName: String, artistName: String, trackTimeMillis: Long, artworkUrl100: String
            ) {
                utilHistoryAdapter.saveTrack(trackName, artistName, trackTimeMillis, artworkUrl100)
                toastIt("добавлен: ${trackName}")
                Timber.d("historyAdapter.saveTrack:${trackName}${artistName}")
            }
        })
        trackRecyclerView.layoutManager = layoutManager
        trackRecyclerView.adapter = trackAdapter
    }

    private fun queryInputListener() { // заполнение с виртуальной клавиатуры
        queryInput.setOnEditorActionListener { textView, actionId, keyEvent -> // заполнение с виртуальной клавиатуры
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val searchText = queryInput.text.toString().trim()
                if (searchText.isNotEmpty()) {
                    utilErrorBox.visibility = View.GONE // исчезновение сообщения с ошибкой
                    preparingForSearch(searchText)
                    toastIt("Поиск: $searchText")
//                    clearTrackAdapterFromHistory()
                }
                hideKeyboard()
                true
            } else {
                false
            }
        }
    }

    private fun queryTextChangedListener() { // заполнение с виртуальной клавиатуры
        queryInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?, start: Int, count: Int, after: Int
            ) {
//                fillTrackAdapterWithHistory()
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().trim()
                clearButton.visibility = if (searchText.isNotEmpty()) View.VISIBLE else View.GONE
                var hasFocus = true
                queryInput.onFocusChangeListener = View.OnFocusChangeListener { _, focus ->
                    hasFocus = focus}
                if (searchText.isEmpty()) {
                    fillTrackAdapterWithHistory()
                    counter++
                } else if (counter > 0) {
                    clearTrackAdapterFromHistory()
                    counter = 0
                }
            }
            override fun afterTextChanged(p0: Editable?) {
                // Реализуйте этот метод с необходимой логикой
            }
        })
    }

    private fun fillTrackAdapterWithHistory() {
        trackAdapter.updateList(cleanTrackList)
        utilHistoryAdapter?.syncTracks()
        utilHistoryAdapter.setRecyclerView(trackRecyclerView)
        trackRecyclerView.scrollToPosition(0)
    }

    private fun clearTrackAdapterFromHistory() {
        utilHistoryAdapter.killList() // чистит адаптер с историей
//        val cleanestTrackList = ArrayList<TrackData>()
        trackAdapter.updateList(cleanTrackList) // чистит адаптер с API
//        utilHistoryAdapter.updateList(cleanestTrackList)
//        trackAdapter.notifyDataSetChanged()
//        trackRecyclerView.scrollToPosition(0)
//        queryInput.clearFocus()
//        historyAdapter.syncTracks()

    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(queryInput.windowToken, 0)
    }

    private fun clearSearchFieldAndHideKeyboard() {
        queryInput.text.clear()
        hideKeyboard()
    }

    private fun toastIt(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun preparingForSearch(searchText: String) {
        loadingIndicator.visibility = View.VISIBLE
        clearButton.isEnabled = false
        Handler(Looper.getMainLooper()).postDelayed({
            performSearch(searchText) { trackItems ->
                loadingIndicator.visibility = View.GONE
                clearButton.isEnabled = true
                trackAdapter.updateList(trackItems)
            }
        }, 1500)
    }

    private var lastQuery: String? = null
    private var lastCallback: ((List<TrackData>) -> Unit)? = null
    private fun performSearch(query: String, callback: (List<TrackData>) -> Unit) {
        // Сохраняем последний запрос и колбэк
        lastQuery = query
        lastCallback = callback
        Timber.d("Запускаем метод performSearch с параметрами Query: $query и Callback")

        // Выполняем поиск с использованием iTunesSearchAPI
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
                            TrackData(
                                track.trackName,
                                track.artistName,
                                track.trackTimeMillis,
                                track.artworkUrl100
                            )
                        }
                    } else {
                        Timber.d("Метод performSearch => response.isSuccessful! => emptyList() таких песен нет")
                        solvingAbsentProblem() // вызываем заглушку о пустом листе запроса
                        emptyList()
                    }
                    // Вызываем колбэк с полученными данными
                    callback(trackData)
                    Timber.d("Метод performSearch => response.isSuccessful! => callback(trackData): $trackData")
                } else {
                    val error = when (response.code()) {
                        400 -> "400 (Bad Request) - ошибка запроса"
                        401 -> "401 (Unauthorized) - неавторизованный запрос"
                        403 -> "403 (Forbidden) - запрещенный запрос"
                        404 -> "404 (Not Found) - не найдено"
                        500 -> "500 (Internal Server Error) - внутренняя ошибка сервера"
                        503 -> "503 (Service Unavailable) - сервис временно недоступен"
                        else -> "(unspecified error) - неустановленная ошибка"
                    }
                    Timber.d(error)
                    toastIt(error)
                    onFailure(
                        call,
                        Throwable(error)
                    ) // Вызываем onFailure с информацией об ошибке
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
//        utilErrorBox = findViewById<LinearLayout>(R.id.util_error_box)
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
//        utilErrorBox = findViewById<LinearLayout>(R.id.util_error_box)
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

}

class UtilTrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackNameTextView: TextView = itemView.findViewById(R.id.track_name_text_view)
    private val artistNameTextView: TextView = itemView.findViewById(R.id.artist_name_text_view)
    private val trackTimeTextView: TextView =
        itemView.findViewById(R.id.track_duration_text_view)
    private val artworkImageView: ImageView = itemView.findViewById(R.id.artwork_image_view)
    private val playButton: LinearLayout = itemView.findViewById(R.id.util_item_track)

    companion object {
        private const val ALBUM_ROUNDED_CORNERS = 8
    }

    fun bind(trackData: TrackData, trackItemClickListener: OnTrackItemClickListener) {
        trackNameTextView.text = trackData.trackName
        artistNameTextView.text = trackData.artistName
        trackTimeTextView.text = formatTrackDuration(trackData.trackTimeMillis)
        loadImage(trackData.artworkUrl100, artworkImageView)

        playButton.setOnClickListener {
            trackItemClickListener.onTrackItemClick(
                trackData.trackName,
                trackData.artistName,
                trackData.trackTimeMillis,
                trackData.artworkUrl100
            )
        }
    }

    private fun formatTrackDuration(trackTimeMillis: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(trackTimeMillis)
        val seconds =
            TimeUnit.MILLISECONDS.toSeconds(trackTimeMillis) - TimeUnit.MINUTES.toSeconds(
                minutes
            )
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun loadImage(imageUrl: String, imageView: ImageView) {
        Glide.with(imageView).load(imageUrl).placeholder(R.drawable.ic_placeholder)
            .transform(RoundedCorners(ALBUM_ROUNDED_CORNERS))
            .error(R.drawable.ic_error_internet)
            .into(imageView)
    }
}

class UtilTrackAdapter(
    private val context: Context,
    private var trackData: MutableList<TrackData>,
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

    fun updateList(newList: List<TrackData>) {
        trackData = newList.toMutableList()
        notifyDataSetChanged()
    }

    fun clearList() {
        trackData.clear()
        notifyDataSetChanged()
    }
}

data class TrackData(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String
)

data class TrackResponse(val results: List<ITunesTrack>)
data class ITunesTrack(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String
)

interface OnTrackItemClickListener {
    fun onTrackItemClick(
        trackName: String, artistName: String, trackTimeMillis: Long, artworkUrl100: String
    )
}

