package com.practicum.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.practicum.playlistmaker.player.domain.MediaPlayerInteractor
import com.practicum.playlistmaker.player.domain.PlayerState
import com.practicum.playlistmaker.utils.AppPreferencesKeys
import com.practicum.playlistmaker.utils.DebounceExtension

class PlayViewModel(private val mediaPlayerInteractor: MediaPlayerInteractor) : ViewModel() {

    private val _screenState = MutableLiveData<ScreenState>(ScreenState.Initial)
    val screenState: LiveData<ScreenState> = _screenState

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

    private fun timerTask() { // добавил проверку для исправления вылета перехода из плеера НАЗАД в список песен
        val playerState = mediaPlayerInteractor.getState()
        if (playerState != PlayerState.INITIAL && playerState != PlayerState.KILL && playerState != PlayerState.ERROR) {
            updatePlayerInfo()
            if (playerState == PlayerState.PLAYING) {
                DebounceExtension(AppPreferencesKeys.CLICK_DEBOUNCE_DELAY, ::timerTask).debounce()
            }
        }
    }

    fun setDataURL(url: String) {
        mediaPlayerInteractor.setDataURL(url)
    }
}