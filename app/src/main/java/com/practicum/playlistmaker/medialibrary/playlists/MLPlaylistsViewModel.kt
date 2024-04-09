package com.practicum.playlistmaker.medialibrary.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.search.domain.TracksRepository

class MLPlaylistsViewModel(private val tracksRepository: TracksRepository) : ViewModel() {

    private val _screenState = MutableLiveData<MLPlaylistsScreenState>(MLPlaylistsScreenState.Loading)
    val screenState: LiveData<MLPlaylistsScreenState> = _screenState

    fun loadFromHistory() {
        val favoritesList = tracksRepository.loadFromHistory()
        _screenState.value = MLPlaylistsScreenState.Ready(favoritesList)
    }
}