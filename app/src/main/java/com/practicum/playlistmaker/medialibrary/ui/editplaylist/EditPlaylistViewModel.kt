package com.practicum.playlistmaker.medialibrary.ui.editplaylist

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.domain.model.Playlist
import com.practicum.playlistmaker.medialibrary.domain.others.PlaylistsInteractor
import com.practicum.playlistmaker.medialibrary.ui.newplaylist.NewPlaylistViewModel
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val playlistId: Int?, private val playlistsInteractor: PlaylistsInteractor
) : NewPlaylistViewModel(playlistsInteractor) {

    private val stateLiveDataEditPlaylist = MutableLiveData<EditPlaylistState>()
    fun observeStateEditPlaylist(): LiveData<EditPlaylistState> = stateLiveDataEditPlaylist

    private lateinit var playlist: Playlist

    init {
        viewModelScope.launch {
            playlist = playlistsInteractor.getPlaylistById(playlistId!!)
            stateLiveDataEditPlaylist.postValue(
                EditPlaylistState.Content(
                    playlist.urlImage,
                    playlist.playlistName,
                    playlist.description
                )
            )
        }
    }

    fun editPlaylist(uri: Uri?, name: String, description: String) {
        playlist.playlistName = name
        playlist.description = description

        if (uri != null) {
            playlist.urlImage = playlistsInteractor.getImageFromPrivateStorage(
                playlistsInteractor.saveImageToPrivateStorage(uri)
            )
        }

        viewModelScope.launch {
            playlistsInteractor.updatePlaylist(playlist)
        }
    }
}