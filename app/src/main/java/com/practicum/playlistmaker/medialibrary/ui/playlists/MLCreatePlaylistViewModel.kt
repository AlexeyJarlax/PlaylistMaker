package com.practicum.playlistmaker.medialibrary.ui.playlists

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.domain.db.Playlist
import com.practicum.playlistmaker.medialibrary.domain.db.PlaylistsInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MLCreatePlaylistViewModel(private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {

    private val _screenState = MutableLiveData<MLCreatePlaylistScreenState>()
    val screenState: LiveData<MLCreatePlaylistScreenState> = _screenState
    init {
        _screenState.value = MLCreatePlaylistScreenState()
    }

    fun createClick() {
        viewModelScope.launch(Dispatchers.IO) {
            val newPlaylist = Playlist(
                id = null,
                title = _screenState.value!!.title,
                description = _screenState.value!!.description,
                poster = _screenState.value!!.uri,
                tracks = "[]",
                count = 0
            )
            playlistsInteractor.upsertPlaylist(newPlaylist)
        }
    }

    fun changeTitle(title: String) {
        _screenState.value = _screenState.value?.copy(title = title)
        _screenState.value = _screenState.value?.copy(isTitleNotEmpty = title.isNotBlank())
        checkChanges()
    }

    fun changeDescription(description: String) {
        _screenState.value = _screenState.value?.copy(description = description)
        checkChanges()
    }

    fun changeUri(uri: Uri) {
        _screenState.value = _screenState.value?.copy(uri = uri)
        checkChanges()
    }

    private fun checkChanges() {
        val isChangesExist = _screenState.value?.title?.isNotBlank() == true ||
                _screenState.value?.description?.isNotBlank() == true ||
                _screenState.value?.uri != null
        _screenState.value = _screenState.value?.copy(isChangesExist = isChangesExist)
    }

}