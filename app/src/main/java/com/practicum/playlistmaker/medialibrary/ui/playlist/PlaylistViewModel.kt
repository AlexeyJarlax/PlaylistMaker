package com.practicum.playlistmaker.medialibrary.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

import com.practicum.playlistmaker.medialibrary.domain.others.PlaylistsInteractor

class PlaylistViewModel(private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistState>()
    fun observeState(): LiveData<PlaylistState> = stateLiveData

    init {
        viewModelScope.launch {
            playlistsInteractor.getAllPlaylists().collect { result ->
                if (result.isEmpty()) {
                    stateLiveData.postValue(PlaylistState.Empty)
                } else {
                    stateLiveData.postValue(PlaylistState.Content(result))
                }
            }
        }
    }
}