package com.practicum.playlistmaker.player.ui

import com.practicum.playlistmaker.player.domain.PlayerState

sealed class ScreenState {
    data object Initial : ScreenState()
    data class Content(
        val playerState: PlayerState = PlayerState.PAUSED
    ): ScreenState()
    data object Error: ScreenState()
    data class  Ready(
        val playerState: PlayerState = PlayerState.READY
    ): ScreenState()
}