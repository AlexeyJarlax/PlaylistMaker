package com.practicum.playlistmaker.medialibrary.ui.newplaylist

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.domain.others.PlaylistsInteractor
import com.practicum.playlistmaker.medialibrary.domain.model.Playlist
import kotlinx.coroutines.launch


open class NewPlaylistViewModel(
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {

    private val statePlaylistLiveData = MutableLiveData<NewPlaylistState>()
    fun observeState(): LiveData<NewPlaylistState> = statePlaylistLiveData

    fun addPlaylist(playlistName: String, playlistDescription: String, imageUri: Uri?) {
        viewModelScope.launch {
            playlistsInteractor.addPlaylist(
                Playlist(
                    0,
                    playlistName,
                    playlistDescription,
                    if (imageUri != null) playlistsInteractor.getImageFromPrivateStorage(
                        playlistsInteractor.saveImageToPrivateStorage(imageUri)
                    ) else null,
                    mutableListOf<Int>(),
                    0
                )
            )
            statePlaylistLiveData.postValue(NewPlaylistState.Success)
        }
    }
}