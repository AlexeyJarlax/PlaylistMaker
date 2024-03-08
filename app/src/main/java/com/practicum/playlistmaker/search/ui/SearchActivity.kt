package com.practicum.playlistmaker.search.ui

import timber.log.Timber
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R

import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.databinding.UtilErrorLayoutBinding
import com.practicum.playlistmaker.player.ui.PlayActivity
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.bindGoBackButton
import com.practicum.playlistmaker.utils.setDebouncedClickListener
import com.practicum.playlistmaker.utils.AppPreferencesKeys
import com.practicum.playlistmaker.utils.DebounceExtension
import com.practicum.playlistmaker.utils.startLoadingIndicator
import com.practicum.playlistmaker.utils.stopLoadingIndicator

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var utilErrorBinding: UtilErrorLayoutBinding
    private var hasFocus = true
    private lateinit var queryInput: EditText
    private lateinit var clearButton: ImageButton
    private lateinit var unitedRecyclerView: RecyclerView
    private lateinit var viewModel: SearchViewModel
    private val trackListFromAPI = ArrayList<Track>()
    private val historyTrackList = ArrayList<Track>()
    private lateinit var adapterForHistoryTracks: AdapterForHistoryTracks
    private lateinit var adapterForAPITracks: AdapterForAPITracks
    private val layoutManager = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.plant(Timber.DebugTree()) // для логирования
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this, SearchViewModel.getViewModelFactory()
        )[SearchViewModel::class.java]
        initViews()
        setupAdapterForHistoryTracks()
        setupAdapterForAPITracks()
        setupObserver()
        clearButton()
        queryTextChangedListener()
        killTheHistory()
        viewModel.setInitialState()
        bindGoBackButton()
    }

    private fun initViews() { // вызовы вьюх
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        utilErrorBinding = UtilErrorLayoutBinding.inflate(layoutInflater)
        binding.root.addView(utilErrorBinding.root)
        utilErrorBinding.root.visibility = View.GONE
        queryInput = binding.searchEditText
        clearButton = binding.clearButton
        unitedRecyclerView = binding.trackRecyclerView
        unitedRecyclerView.layoutManager = layoutManager
    }

    // устанавливаем адаптер на треки из АйТюнс
    private fun setupAdapterForAPITracks() {
        adapterForAPITracks = AdapterForAPITracks {
            viewModel.saveToHistory(it)
            val intent = Intent(this@SearchActivity, PlayActivity::class.java)
            intent.putExtra(AppPreferencesKeys.AN_INSTANCE_OF_THE_TRACK_CLASS, it)
            startActivity(intent)
        }
        adapterForAPITracks.tracks = trackListFromAPI
    }

    // устанавливаю адаптер на треки из истории сохранений
    private fun setupAdapterForHistoryTracks() {
        adapterForHistoryTracks = AdapterForHistoryTracks {
            viewModel.saveToHistoryAndRefresh(it)
            val intent = Intent(this@SearchActivity, PlayActivity::class.java)
            intent.putExtra(AppPreferencesKeys.AN_INSTANCE_OF_THE_TRACK_CLASS, it)
            startActivity(intent)
        }
        adapterForHistoryTracks.searchHistoryTracks = historyTrackList
    }

    //********************************** устанавливаем наблюдатель за изменениями в состоянии экрана

    private fun setupObserver() {
        viewModel.screenState.observe(this@SearchActivity) { screenState ->
            when (screenState) {
                SearchScreenState.InitialState -> {
                    Timber.d("=== SearchScreenState.InitialState")
                    unitedRecyclerView.isVisible = false
                    binding.killTheHistory.isVisible = false
                }

                SearchScreenState.Loading -> {
                    Timber.d("=== SearchScreenState.Loading")
                    hideKeyboard()
                    startLoadingIndicator()
                    unitedRecyclerView.isVisible = false
                    binding.killTheHistory.isVisible = false
                }

                is SearchScreenState.ShowHistory -> {
                    Timber.d("=== SearchScreenState.ShowHistory")
                    showTracksFromHistory(screenState.historyList)
                    unitedRecyclerView.isVisible = true
                    binding.killTheHistory.isVisible = true
                    stopLoadingIndicator()
                }

                is SearchScreenState.SearchAPI -> {
                    Timber.d("=== SearchScreenState.SearchAPI")
                    showSearchFromAPI(screenState.searchAPIList)
                    unitedRecyclerView.isVisible = true
                    binding.killTheHistory.isVisible = false
                    stopLoadingIndicator()
                }

                is SearchScreenState.NoResults -> {
                    Timber.e("=== SearchScreenState.NoResults")
                    unitedRecyclerView.isVisible = false
                    binding.killTheHistory.isVisible = false
                    solvingThisProblemWith(AppPreferencesKeys.RESULTS) {}
                    stopLoadingIndicator()
                }

                is SearchScreenState.Error -> {
                    Timber.e("=== SearchScreenState.Error")
                    unitedRecyclerView.isVisible = false
                    binding.killTheHistory.isVisible = false
                    solvingThisProblemWith(AppPreferencesKeys.INTERNET) {
                        viewModel.searchRequestFromViewModel((queryInput.text.toString().trim()), true)
                    }
                    stopLoadingIndicator()
                }
            }
        }
    }

    //****** обработка функций на показ истории сохраненных треков, удаление истории, поиск, очистка

    @SuppressLint("NotifyDataSetChanged") // Историй показывают, красивое
    private fun showTracksFromHistory(historyList: List<Track>) {
        if (historyList.isNotEmpty() && historyList != historyTrackList) {
            historyTrackList.clear()
            historyTrackList.addAll(historyList)
            adapterForHistoryTracks.notifyDataSetChanged()
            unitedRecyclerView.adapter = adapterForHistoryTracks
            viewModel.showActiveList()
        }
    }

    private fun killTheHistory() {
        binding.killTheHistory.setDebouncedClickListener {
            viewModel.killHistory()
            historyTrackList.clear()
            binding.killTheHistory.isVisible = false
            adapterForHistoryTracks.notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showSearchFromAPI(resultsList: List<Track>) {
        if (resultsList.isNotEmpty()) {
            Timber.d("=== class SearchActivity => fun showSearchResults( ${resultsList} )")
            trackListFromAPI.clear()
            trackListFromAPI.addAll(resultsList)
            adapterForAPITracks.notifyDataSetChanged()
            unitedRecyclerView.adapter = adapterForAPITracks
        } else {
                viewModel.setNoResultsState()
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(queryInput.windowToken, 0)
    }

    private fun clearButton() {
        clearButton.setDebouncedClickListener {
            queryInput.text.clear()
            viewModel.showHistoryFromViewModel()
        }
    }

    //************************************************************** обработка ввода в поле поиска

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
                Timber.d("=== class SearchActivity  => (viewModel.searchDebounce( ${searchText} ))")
                if (hasFocus && searchText.isEmpty()) {  // обработка ввода без нажатий
                    showToUserHistoryOfOldTracks()
                } else {
                    startToSearchTrackWithDebounce()
                }
            }

            override fun afterTextChanged(editable: Editable?) {
            }
        })
        // Фокус + ЖЦ вход в приложение queryInput пуст
        queryInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && queryInput.text.isEmpty()) {
                showToUserHistoryOfOldTracks()
            } else if (queryInput.text.isNotEmpty()) {
            }
        }
        // обработка ввода с нажатием DONE
        queryInput.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val searchText = queryInput.text.toString().trim()
                if (searchText.isNotEmpty()) {
                    startToSearchTrackRightAway() // ищем песню сразу
                }
                hideKeyboard()
                true
            } else {
                false
            }
        }
    }

    //************************************** отправляем реакции на клик, ввод, и тп. во viewModel
    private fun showToUserHistoryOfOldTracks() {
        viewModel.showHistoryFromViewModel()
    }

    private val twoSecondDebounceSearch =  // обработка задержки в 2 сек
        DebounceExtension(AppPreferencesKeys.TWO_SECONDS) {
            viewModel.searchRequestFromViewModel((queryInput.text.toString().trim()), false)
        }

    private fun startToSearchTrackWithDebounce() { // задержка для поиска во время ввода
        twoSecondDebounceSearch.debounce()
    }

    private fun startToSearchTrackRightAway() { // ищем трек сразу
        viewModel.searchRequestFromViewModel((queryInput.text.toString().trim()), false)
    }

    //****************************************** решаем проблемы с отсутствием результата поиска треков или интернета

    fun solvingThisProblemWith(problemTipo: String, sendRequestForDoReserch: () -> Unit) {
        val utilErrorBox = findViewById<LinearLayout>(R.id.utilErrorBox)
        val errorIcon = findViewById<ImageView>(R.id.error_icon)
        val errorTextWeb = findViewById<TextView>(R.id.error_text_web)
        val retryButton = findViewById<Button>(R.id.retry_button)

        when (problemTipo) {
            AppPreferencesKeys.INTERNET -> {
                errorIcon.setImageResource(R.drawable.ic_error_internet)
                errorTextWeb.text = resources.getString(R.string.error_text_web)
                retryButton.visibility = View.VISIBLE
                retryButton.setDebouncedClickListener {
                    sendRequestForDoReserch() // тут отправляем на повторный поиск
                    utilErrorBox.visibility = View.GONE
                }
            }

            AppPreferencesKeys.RESULTS -> {
                errorIcon.setImageResource(R.drawable.ic_error_notfound)
                errorTextWeb.text = resources.getString(R.string.nothing_was_found)
                retryButton.visibility = View.GONE
            }

            else -> {
                retryButton.visibility = View.GONE
            }
        }

        utilErrorBox.visibility = View.VISIBLE
        utilErrorBox.setDebouncedClickListener {
            utilErrorBox.visibility = View.GONE
        }
    }
}