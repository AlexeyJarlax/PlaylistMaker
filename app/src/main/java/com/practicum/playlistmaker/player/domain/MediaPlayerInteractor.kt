package com.practicum.playlistmaker.player.domain

interface MediaPlayerInteractor {

    fun getState(): PlayerState
    fun getPlayerReady()
    fun getPlaybackPosition(): Int

    fun play()
    fun pause()
    fun stop()
}