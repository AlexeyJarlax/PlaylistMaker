package com.practicum.playlistmaker.search.ui

import android.app.Application
import androidx.core.util.Consumer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.models.TracksResponse
import timber.log.Timber

class SearchViewModel(private val tracksInteractor: TracksInteractor) : ViewModel() {

    private val _screenState = MutableLiveData<SearchScreenState>(SearchScreenState.Loading)
    val screenState: LiveData<SearchScreenState> = _screenState
    private var oldSearchText: String = ""
    private val historyTrackList = ArrayList<Track>()
    init {showHistoryFromViewModel()}

    fun showHistoryFromViewModel() {
        val searchHistory = tracksInteractor.loadFromHistory()
        _screenState.value = SearchScreenState.ShowHistory(searchHistory)
        Timber.d("=== class SearchViewModel => loadAndSetSearchHistory => fun searchRequest(${searchHistory})")
    }

    fun searchRequestFromViewModel(searchText: String, rebootingFromError: Boolean) {
        Timber.d("=== class SearchViewModel => fun searchRequestFromViewModel (${searchText})")
        if (!rebootingFromError && oldSearchText == searchText) return
        oldSearchText = searchText
        _screenState.value = SearchScreenState.Loading
        val consumer = Consumer<TracksResponse> { tracksResponse ->
            if (tracksResponse.resultCode == 200) {
                _screenState.postValue(SearchScreenState.SearchAPI(tracksResponse.results))
            } else _screenState.postValue(SearchScreenState.Error)
        }
        tracksInteractor.searchTracks(searchText, consumer)
    }

    fun killHistory() {
        tracksInteractor.killHistory()
        showHistoryFromViewModel()
    }

    fun saveToHistory(track: Track) {
        tracksInteractor.saveToHistory(track)
//        showActiveList()
    }

    fun saveToHistoryAndRefresh(track: Track) {
        tracksInteractor.saveToHistory(track)
        showActiveList()
    }

    fun showActiveList() { // метод для того, чтобы после клика по списку сохраненных треков список пересобирался перенося трек на 0 место
        val activeList = tracksInteractor.getActiveList()
        if (_screenState.value !is SearchScreenState.ShowHistory || (activeList.isNotEmpty() && activeList != historyTrackList)) {
            _screenState.value = SearchScreenState.ShowHistory(activeList)
        }
    }

     override fun onCleared() {
        super.onCleared()
    }

    fun setInitialState() {
        _screenState.value = SearchScreenState.InitialState
    }

    fun setNoResultsState() {
        _screenState.value = SearchScreenState.NoResults
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as Application
                SearchViewModel(
                    tracksInteractor = Creator.provideTracksInteractor(application)
                )
            }
        }
    }

}

