package com.practicum.playlistmaker.medialibrary.ui.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.search.domain.TracksRepository
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.domain.db.PlaylistsInteractor
import kotlinx.coroutines.launch

class MLPlaylistsViewModel(private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {

    private val _screenState =
        MutableLiveData<MLPlaylistsScreenState>(MLPlaylistsScreenState.Loading)
    val screenState: LiveData<MLPlaylistsScreenState> = _screenState

    fun loadFromHistory() {
//        val favoritesList = tracksRepository.loadFromHistory()
//        _screenState.value = MLPlaylistsScreenState.Ready(favoritesList)

    }

    init {
        viewModelScope.launch {
            playlistsInteractor.playlists().collect { list ->
                _screenState.value = MLPlaylistsScreenState.Content(list)
            }
        }
    }
}