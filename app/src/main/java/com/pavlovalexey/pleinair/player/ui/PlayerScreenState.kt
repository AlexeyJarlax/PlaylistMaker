package com.practicum.playlistmaker.player.ui

import com.practicum.playlistmaker.player.domain.PlayerState

sealed class PlayerScreenState() {

    data object Initial: PlayerScreenState()

    data class Content(
        val playerState: PlayerState = PlayerState.PAUSED,
        val playbackPosition: Int = 0
    ): PlayerScreenState()

    data class Error(
        val playerState: PlayerState = PlayerState.ERROR,
        val playbackPosition: Int = 0
    ): PlayerScreenState()

    data class Ready(
        val playerState: PlayerState = PlayerState.READY,
        val playbackPosition: Int = 0
    ): PlayerScreenState()
}