package com.practicum.playlistmaker.ui

/* === Памятка о содержании файла:
SearchActivity - активити и вся обработка поискового запроса юзера.
UtilTrackViewHolder - холдер для RecyclerView, отображающий информацию о треках.
AdapterForAPITracks - адаптер для RecyclerView, отображающий информацию о треках.

=== Этапы поиска:
1. этап: считываем поле поиска
1.1 ввод в queryInput.setOnEditorActionListener и кнопка DONE  ===> запуск 2 этапа
1.2 ввод в queryInput.addTextChangedListener  ===> запуск 2 этапа через 2 секунды
2. этап: передаем searchText в searchStep1 => searchStep2, для активации progressBar ===> запуск 3 этапа
3. этап: передаем searchText в searchStep3 => вызываем TrackResponse => заполняем Track  ===> вывод списка песен, соответствующих запросу
3.1 : performSearch => [возникла ошибка с вызовом TrackResponse] => Запускаем метод solvingConnectionProblem() ===> Запускаем повторно 2 этап
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
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.HistoryTrackClickListener
import com.practicum.playlistmaker.data.network.ArtworkUrlLoader
import com.practicum.playlistmaker.presentation.AdapterForHistoryTracks
import com.practicum.playlistmaker.domain.api.InteractorForTracksList
import com.practicum.playlistmaker.data.preferences.AppPreferencesKeys
import com.practicum.playlistmaker.domain.api.RepositoryForSelectedTrack
import com.practicum.playlistmaker.domain.api.ProviderForSelectedTrack
import com.practicum.playlistmaker.presentation.DebounceExtension
import com.practicum.playlistmaker.presentation.openThread
import com.practicum.playlistmaker.presentation.setDebouncedClickListener
import com.practicum.playlistmaker.domain.models.TracksList
import com.practicum.playlistmaker.presentation.buttonBack
import com.practicum.playlistmaker.presentation.solvingAbsentProblem
import com.practicum.playlistmaker.presentation.stopLoadingIndicator
import timber.log.Timber
import java.util.concurrent.TimeUnit
import kotlinx.serialization.json.Json

class SearchActivity : AppCompatActivity(), ProviderForSelectedTrack {

    private lateinit var trackUseCase: RepositoryForSelectedTrack  // TrackUseCase интерфейс

    private var hasFocus = true
    private lateinit var queryInput: EditText
    private lateinit var clearButton: ImageButton
    private lateinit var trackRecyclerView: RecyclerView
    private lateinit var adapterForAPITracks: AdapterForAPITracks
    private val cleanTrackList = ArrayList<TracksList>()
    private lateinit var utilErrorBox: View
    private lateinit var adapterForHistoryTracks: AdapterForHistoryTracks
    private lateinit var searchHistoryNotification: TextView
    private lateinit var killTheHistory: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.plant(Timber.DebugTree()) // для логирования ошибок
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initViews()
        clearButton()
        callAdapterForHistoryTracks()
        setupRecyclerViewAndAdapter()
        queryTextChangedListener()
        queryInputListener()
        fillTrackAdapter()
        killTheHistory()
        buttonBack()
    }

    private fun initViews() {
        clearButton = findViewById(R.id.clearButton)
        queryInput = findViewById(R.id.search_edit_text)
        trackRecyclerView = findViewById(R.id.track_recycler_view)
        utilErrorBox = findViewById<LinearLayout>(R.id.util_error_box)
        searchHistoryNotification = findViewById(R.id.you_were_looking_for)
        killTheHistory = findViewById(R.id.kill_the_history)
    }

    override fun provideTrackUseCase(): RepositoryForSelectedTrack {  // TrackUseCase интерфейс
        return trackUseCase
    }

    private fun callAdapterForHistoryTracks() {
        adapterForHistoryTracks = AdapterForHistoryTracks(this, object : HistoryTrackClickListener {
            override fun onTrackItemClick(track: TracksList) {
                adapterForHistoryTracks.saveTrack(track)
            }
        })
        adapterForHistoryTracks.setRecyclerView(trackRecyclerView)
    }

    private fun setupRecyclerViewAndAdapter() {
        val layoutManager = LinearLayoutManager(this)
        adapterForAPITracks =
            AdapterForAPITracks(this, cleanTrackList, object : HistoryTrackClickListener {
                override fun onTrackItemClick(track: TracksList) {
                    adapterForHistoryTracks.saveTrack(track)
                    Timber.d("historyAdapter.saveTrack:${track.trackName}${track.artistName}")
                }
            })
        trackRecyclerView.layoutManager = layoutManager
        trackRecyclerView.adapter = adapterForAPITracks
    }

    //******************************************************** ввод в поле поиска и обработка ввода
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
                    searchWithDebounce() // пытаемся искать песни во время паузы 2 секунды ввода
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
    private val twoSecondDebounceSearch =
        DebounceExtension(AppPreferencesKeys.SEARCH_DEBOUNCE_DELAY) {
            searchStep1Preparation(queryInput.text.toString().trim())
        }

    private fun searchWithDebounce() {
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
            val trackInteractor = Creator.provideTrackInteractor()
            trackInteractor.searchStep3useAPI(
                searchText,
                object : InteractorForTracksList.TrackConsumer {
                    override fun consume(foundTrack: List<TracksList>) {
                        Timber.d("=== performSearch в потоке: ${Thread.currentThread().name}")
                        runOnUiThread {
                            if (foundTrack.isEmpty()) {
                                    solvingAbsentProblem()
                                }
                            adapterForAPITracks.updateList(foundTrack)
                            adapterForAPITracks.setRecyclerView(trackRecyclerView)
                            trackRecyclerView.visibility = View.VISIBLE
                            Timber.d("=== adapter и Recycler в потоке: ${Thread.currentThread().name}")
                            stopLoadingIndicator()
                        }
                    }
                })
        }
    }

    private fun clearButton() {
        clearButton.setDebouncedClickListener {
            queryInput.text.clear()
        }
    }

}

//******************************************************************************* Adapter и Recycler
class UtilTrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackNameTextView: TextView = itemView.findViewById(R.id.track_name_text_view)
    private val artistNameTextView: TextView = itemView.findViewById(R.id.artist_name_text_view)
    private val trackTimeTextView: TextView =
        itemView.findViewById(R.id.track_duration_text_view)
    private val artworkImageView: ImageView = itemView.findViewById(R.id.artwork_image_view)
    private val playButton: LinearLayout = itemView.findViewById(R.id.util_item_track)

    fun bind(track: TracksList, trackItemClickListener: HistoryTrackClickListener) {
        trackNameTextView.text = track.trackName ?: ""
        artistNameTextView.text = track.artistName ?: ""
        trackTimeTextView.text = track.trackTimeMillis?.let { formatTrackDuration(it) } ?: ""
        track.artworkUrl100?.let {
            ArtworkUrlLoader(context = itemView.context).loadImage(
                it,
                artworkImageView
            )
        }

        playButton.setDebouncedClickListener {
            trackItemClickListener.onTrackItemClick(track)
            val intent = Intent(itemView.context, PlayActivity::class.java)
            val trackJson = Json.encodeToString(TracksList.serializer(), track)
            intent.putExtra("trackJson", trackJson)
            itemView.context.startActivity(intent)
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

}

class AdapterForAPITracks(
    private val context: Context,
    private var trackData: List<TracksList>,
    private val trackItemClickListener: HistoryTrackClickListener
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

    fun updateList(newList: List<TracksList>) {
        trackData = newList
        notifyDataSetChanged()
    }

    fun clearList() {
        val newList: MutableList<TracksList> = mutableListOf()
        trackData = newList
        notifyDataSetChanged()
    }
}

