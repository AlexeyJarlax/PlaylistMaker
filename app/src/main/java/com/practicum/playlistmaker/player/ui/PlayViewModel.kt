package com.practicum.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.player.domain.MediaPlayerInteractor
import com.practicum.playlistmaker.player.domain.PlayerState
import com.practicum.playlistmaker.utils.AppPreferencesKeys
import com.practicum.playlistmaker.utils.DebounceExtension

class PlayViewModel(private val mediaPlayerInteractor: MediaPlayerInteractor) : ViewModel() {

    private val _screenState = MutableLiveData<ScreenState>(ScreenState.Initial)
    val screenState: LiveData<ScreenState> = _screenState

    init {
      mediaPlayerInteractor.getPlayerReady()
    }
    private fun playerPlay() {
        mediaPlayerInteractor.play()
        DebounceExtension(AppPreferencesKeys.CLICK_DEBOUNCE_DELAY, ::timerTask).debounce()
    }

    private fun playerPause() {
        mediaPlayerInteractor.pause()
        updatePlayerInfo()
    }

    private fun updatePlayerInfo() {
        val tmpPlayerState = mediaPlayerInteractor.getState()
        _screenState.value = ScreenState.Content(tmpPlayerState)
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

    private fun timerTask() {
        updatePlayerInfo()
        if (mediaPlayerInteractor.getState() == PlayerState.PLAYING) {
            DebounceExtension(AppPreferencesKeys.CLICK_DEBOUNCE_DELAY, ::timerTask).debounce()
        }
    }

    companion object {
            fun getViewModelFactory(previewUrl: String?): ViewModelProvider.Factory = viewModelFactory {
                initializer {
                    PlayViewModel(
                        mediaPlayerInteractor = Creator.provideMediaPlayerInteractor(previewUrl ?: "")
                    )
                }
            }

    }
}