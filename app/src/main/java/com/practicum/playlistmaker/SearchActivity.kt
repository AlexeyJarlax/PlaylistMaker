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
import com.practicum.playlistmaker.util.AdapterForHistoryTracks
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
    var counter = 0 // счетчик сбросов
    private lateinit var searchHistoryNotification: TextView
    private lateinit var clearTheHistory: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.plant(Timber.DebugTree()) // для логирования ошибок
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setupOneLineViews()
        clearButton()
        backToMain()
        callAdapterForHistoryTracks()
        setupRecyclerViewAndAdapter()

//        clearTrackAdapterFromHistory()
//        adapterForHistoryTracks?.setRecyclerView(trackRecyclerView)
//        adapterForHistoryTracks?.syncTracks()
//        trackRecyclerView.scrollToPosition(0)

        queryTextChangedListener()
        queryInputListener()
        fillTrackAdapterWithHistory()
//        clearTrackAdapterFromHistory()


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
        clearTheHistory = findViewById(R.id.clear_the_history)
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

    private fun callAdapterForHistoryTracks() {
        adapterForHistoryTracks = AdapterForHistoryTracks(this, object : OnTrackItemClickListener {
            override fun onTrackItemClick(
                trackName: String, artistName: String, trackTimeMillis: Long, artworkUrl100: String
            ) {
            }
        })
        adapterForHistoryTracks.setRecyclerView(trackRecyclerView)
    }

    private fun setupRecyclerViewAndAdapter() {
        val layoutManager = LinearLayoutManager(this)
        adapterForAPITracks =
            AdapterForAPITracks(this, cleanTrackList, object : OnTrackItemClickListener {
                override fun onTrackItemClick(
                    trackName: String,
                    artistName: String,
                    trackTimeMillis: Long,
                    artworkUrl100: String
                ) {
                    adapterForHistoryTracks.saveTrack(
                        trackName, artistName, trackTimeMillis, artworkUrl100
                    )
                    toastIt("добавлен: ${trackName}")
                    Timber.d("historyAdapter.saveTrack:${trackName}${artistName}")
                }
            })
        trackRecyclerView.layoutManager = layoutManager
        trackRecyclerView.adapter = adapterForAPITracks
    }

//    private fun queryTextChangedListener() {
//        queryInput.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
//                // No action needed before text changes
//            }
//
//            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
//                val searchText = queryInput.text.toString().trim()
//                clearButton.visibility = if (searchText.isNotEmpty()) View.VISIBLE else View.GONE
//
//                queryInput.onFocusChangeListener = View.OnFocusChangeListener { _, focus ->
//                    if (focus) {
//                        hasFocus = true
//                    }
//                }
//                if (hasFocus && charSequence?.isEmpty() == true) {
//                    fillTrackAdapterWithHistory()
//                    showHistoryViews()
//                } else if (counter > 0) {
//                    clearTrackAdapterFromHistory()
//                    hideHistoryViews()
//                    counter = 0
//                }
//            }
//
//            override fun afterTextChanged(editable: Editable?) {
//                // No action needed after text changes
//            }
//        })
//    }

    private fun queryTextChangedListener() {
        queryInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = queryInput.text.toString().trim()
                clearButton.visibility = if (searchText.isNotEmpty()) View.VISIBLE else View.GONE
                if (hasFocus && searchText.isEmpty()) {  // обработка ввода без нажатий
                    showHistoryViews()
                } else {
                    hideHistoryViews()
                }
            }

            override fun afterTextChanged(editable: Editable?) {
            }
        })

        // Фокус + ЖЦ вход в приложение queryInput пуст
        queryInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && queryInput.text.isEmpty()) {
                showHistoryViews()
            } else if (queryInput.text.isNotEmpty()) {
                hideHistoryViews()
            }
        }
    }


    private fun queryInputListener() { // обработка ввода с нажатием
        queryInput.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val searchText = queryInput.text.toString().trim()
                if (searchText.isNotEmpty()) {
                    utilErrorBox.visibility = View.GONE
                    clearTrackAdapterFromHistory()

                    preparingForSearch(searchText)
                    toastIt("Поиск: $searchText")
                }
                hideKeyboard()
                true
            } else {
                false
            }
        }
    }

    private fun showHistoryViews() {
        fillTrackAdapterWithHistory()
        counter++
        trackRecyclerView.visibility = View.VISIBLE
        searchHistoryNotification.visibility = View.VISIBLE
        clearTheHistory.visibility = View.VISIBLE
    }

    private fun hideHistoryViews() {
        clearTrackAdapterFromHistory()
        counter = 0
        trackRecyclerView.visibility = View.GONE
        searchHistoryNotification.visibility = View.GONE
        clearTheHistory.visibility = View.GONE
    }

    private fun fillTrackAdapterWithHistory() {
        clearTrackAdapterFromHistory()
        adapterForHistoryTracks?.setRecyclerView(trackRecyclerView)
        adapterForHistoryTracks?.syncTracks()
        trackRecyclerView.scrollToPosition(0)
    }

    private fun clearTrackAdapterFromHistory() {
        adapterForHistoryTracks.killList() // чистит адаптер с историей
//        trackAdapter.updateList(cleanTrackList) // чистит адаптер с API
//        trackAdapter.clearList()
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
                adapterForAPITracks.updateList(trackItems)
                adapterForAPITracks.setRecyclerView(trackRecyclerView)
                trackRecyclerView.visibility = View.VISIBLE
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
                        call, Throwable(error)
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

}

class UtilTrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackNameTextView: TextView = itemView.findViewById(R.id.track_name_text_view)
    private val artistNameTextView: TextView = itemView.findViewById(R.id.artist_name_text_view)
    private val trackTimeTextView: TextView = itemView.findViewById(R.id.track_duration_text_view)
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
        val seconds = TimeUnit.MILLISECONDS.toSeconds(trackTimeMillis) - TimeUnit.MINUTES.toSeconds(
            minutes
        )
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun loadImage(imageUrl: String, imageView: ImageView) {
        Glide.with(imageView).load(imageUrl).placeholder(R.drawable.ic_placeholder)
            .transform(RoundedCorners(ALBUM_ROUNDED_CORNERS)).error(R.drawable.ic_error_internet)
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

