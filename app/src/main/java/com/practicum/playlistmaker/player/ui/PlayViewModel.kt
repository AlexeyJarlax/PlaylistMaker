package com.practicum.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.practicum.playlistmaker.player.domain.MediaPlayerInteractor
import com.practicum.playlistmaker.player.domain.PlayerState
import com.practicum.playlistmaker.utils.AppPreferencesKeys
import com.practicum.playlistmaker.utils.DebounceExtension
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PlayViewModel(private val mediaPlayerInteractor: MediaPlayerInteractor) : ViewModel() {

    private val _screenState = MutableLiveData<ScreenState>(ScreenState.Initial)
    val screenState: LiveData<ScreenState> = _screenState

    private var job: Job? = null

    fun playerPlay() {
        mediaPlayerInteractor.play()
        startTimer()
    }

    fun playerPause() {
        mediaPlayerInteractor.pause()
        cancelTimer()
    }

    private fun startTimer() {
        job?.cancel()
        job = viewModelScope.launch {
            flow {
                while (isActive) {
                    emit(Unit)
                    delay(300)
                }
            }.collect {
                updatePlayerInfo()
            }
        }
    }

    private fun cancelTimer() {
        job?.cancel()
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
        cancelTimer()
    }

    fun setDataURL(url: String) {
        mediaPlayerInteractor.setDataURL(url)
    }
}