package com.practicum.playlistmaker.player.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.domain.others.FavoritesTracksInteractor
import com.practicum.playlistmaker.medialibrary.domain.model.Playlist
import com.practicum.playlistmaker.medialibrary.domain.others.PlaylistsInteractor
import com.practicum.playlistmaker.medialibrary.ui.allplaylists.AllPlaylistsState
import com.practicum.playlistmaker.player.domain.MediaPlayerInteractor
import com.practicum.playlistmaker.player.domain.PlayerState
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.AppPreferencesKeys
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    private val favoritesInteractor: FavoritesTracksInteractor,
    private val playlistsInteractor: PlaylistsInteractor,
    private val track: Track
) : ViewModel() {

    private val _screenState = MutableLiveData<PlayerScreenState>(PlayerScreenState.Initial)
    val screenState: LiveData<PlayerScreenState> = _screenState

    private val _isFavoriteTrack = MutableLiveData(false)
    val isFavoriteTrack: LiveData<Boolean> = _isFavoriteTrack

    private val _statePlaylist = MutableLiveData<AllPlaylistsState>()
    val statePlaylist: LiveData<AllPlaylistsState> = _statePlaylist

    private val _stateAddTrack = MutableLiveData<Boolean?>(null)
    val stateAddTrack: LiveData<Boolean?> = _stateAddTrack

    private var trackId = track.trackId ?: -1

    private var timerJob: Job? = null

    init {
        mediaPlayerInteractor.getPlayerReady()
        viewModelScope.launch {
            favoritesInteractor.getTracksIDs().collect { trackIds ->
                _isFavoriteTrack.value = trackIds.contains(trackId)
            }
        }

        viewModelScope.launch {
            playlistsInteractor.getAllPlaylists().collect { result ->
                if (result.isEmpty()) {
                    _statePlaylist.postValue(AllPlaylistsState.Empty(result))
                } else {
                    _statePlaylist.postValue(AllPlaylistsState.Content(result))
                }
            }
        }
    }

    fun upsertFavoriteTrack(track: Track) {
        Log.d("=== LOG ===", "=== PlayViewModel > changeFavoriteClick(track: Track)")
        viewModelScope.launch {
            if (_isFavoriteTrack.value == true) {
                favoritesInteractor.deleteTrack(track)
                _isFavoriteTrack.postValue(false)
            } else {
                favoritesInteractor.addTrack(track)
                _isFavoriteTrack.postValue(true)
            }
        }
    }

    private fun playerPlay() {
        mediaPlayerInteractor.play()
        startTimer()
    }

    private fun playerPause() {
        mediaPlayerInteractor.pause()
        updatePlayerInfo()
    }

    fun getState(): PlayerState {
        return mediaPlayerInteractor.getState()
    }

    private fun updatePlayerInfo() {
        val playerState = mediaPlayerInteractor.getState()
        val playbackPosition = mediaPlayerInteractor.getPlaybackPosition()
        _screenState.value = PlayerScreenState.Content(playerState, playbackPosition)
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
        timerJob?.cancel()
    }

    fun setDataURL(track: Track) {
        mediaPlayerInteractor.resetPlayer()
        track.previewUrl?.let { mediaPlayerInteractor.setDataURL(it) }
        trackId = track.trackId ?: -1
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (mediaPlayerInteractor.getState() == PlayerState.PLAYING) {
                delay(AppPreferencesKeys.THREE_HUNDRED_MILLISECONDS)
                updatePlayerInfo()
            }
        }
    }

    fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        if (!playlist.tracksIds.contains(track.trackId)) {
            viewModelScope.launch {
                playlistsInteractor.updatePlaylistAndAddTrack(track, playlist)
                _stateAddTrack.postValue(true)
            }
        } else {
            _stateAddTrack.postValue(false)
        }
    }

    fun deleteValueStateAddTrack() {
        _stateAddTrack.postValue(null)
    }
}