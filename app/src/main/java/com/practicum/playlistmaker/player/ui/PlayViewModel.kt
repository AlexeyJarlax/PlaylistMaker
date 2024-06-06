package com.practicum.playlistmaker.player.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.domain.db.FavoritesInteractor
import com.practicum.playlistmaker.medialibrary.domain.db.Playlist
import com.practicum.playlistmaker.medialibrary.domain.db.PlaylistsInteractor
import com.practicum.playlistmaker.player.domain.AddResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import com.practicum.playlistmaker.player.domain.MediaPlayerInteractor
import com.practicum.playlistmaker.player.domain.PlayerState
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.AppPreferencesKeys
import com.practicum.playlistmaker.utils.AppPreferencesKeys.ONE_SECOND
import com.practicum.playlistmaker.utils.DebounceExtension
import com.practicum.playlistmaker.utils.toIntList
import kotlinx.coroutines.Dispatchers

class PlayViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {

    private val _screenState = MutableLiveData<ScreenState>(ScreenState.Initial)
    val screenState: LiveData<ScreenState> = _screenState
    private val _isFavoriteTrack = MutableLiveData(false)
    val isFavoriteTrack: LiveData<Boolean> = _isFavoriteTrack
    private var trackId = -1
    private val _playlists = MutableLiveData<List<Playlist>>(emptyList())
    val playlists: LiveData<List<Playlist>> = _playlists
    private val _addResult = MutableLiveData(AddResult(null, null))
    val addResult: LiveData<AddResult> = _addResult

    init {
        _screenState.value = ScreenState.Content()
        viewModelScope.launch {
            favoritesInteractor.getTrackIds().collect { trackIds ->
                _isFavoriteTrack.value = trackIds.contains(trackId)
            }
        }
        viewModelScope.launch {
            playlistsInteractor.playlists().collect { list ->
                _playlists.value = list
            }
        }
    }

    fun upsertFavoriteTrack(track: Track) {
        Log.d("=== LOG ===", "=== PlayViewModel > changeFavoriteClick(track: Track)")
        viewModelScope.launch {
            favoritesInteractor.upsertTrack(track)
        }
    }

    private fun playerPlay() {
        mediaPlayerInteractor.play()
        DebounceExtension(AppPreferencesKeys.THREE_HUNDRED_MILLISECONDS, ::timerTask).debounce()
    }

    private fun playerPause() {
        mediaPlayerInteractor.pause()
        updatePlayerInfo()
    }

    fun getState(): PlayerState {
        val state = mediaPlayerInteractor.getState()
        return state
    }

    private fun updatePlayerInfo() {
        val playerState = mediaPlayerInteractor.getState()
        val playbackPosition = mediaPlayerInteractor.getPlaybackPosition()
        _screenState.value = ScreenState.Content(playerState, playbackPosition)
    }

    fun playBtnClick() {
        if (mediaPlayerInteractor.getState() == PlayerState.PLAYING) playerPause()
        else playerPlay()
    }

    fun onActivityPaused() {
        playerPause()
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayerInteractor.stop()
    }

    fun setDataURL(track: Track) {
        mediaPlayerInteractor.resetPlayer()
        track.previewUrl?.let { mediaPlayerInteractor.setDataURL(it) }
        trackId = track.trackId!!
    }

    private fun timerTask() { // добавил проверку для исправления вылета перехода из плеера НАЗАД в список песен
        val playerState = mediaPlayerInteractor.getState()
        if (playerState != PlayerState.INITIAL && playerState != PlayerState.KILL && playerState != PlayerState.ERROR) {
            updatePlayerInfo()
            if (playerState == PlayerState.PLAYING) {
                DebounceExtension(AppPreferencesKeys.CLICK_DEBOUNCE_DELAY, ::timerTask).debounce()
            }
        }
    }

    fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        val playlistTracks = playlist.tracks.toIntList()
        if (playlistTracks.contains(trackId)) {
            viewModelScope.launch(Dispatchers.IO) {
                _addResult.postValue(AddResult(successful = false, playlist = playlist.title))
                delay(ONE_SECOND)
                _addResult.postValue(AddResult(successful = null, playlist = null))
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                playlistsInteractor.addTrackToPlaylist(track = track, playlist = playlist)
                _addResult.postValue(AddResult(successful = true, playlist = playlist.title))
                delay(ONE_SECOND)
                _addResult.postValue(AddResult(successful = null, playlist = null))
            }
        }
    }
}