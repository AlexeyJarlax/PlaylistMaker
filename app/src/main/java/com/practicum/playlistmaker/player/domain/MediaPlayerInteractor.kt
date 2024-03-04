package com.practicum.playlistmaker.player.domain

interface MediaPlayerInteractor {

    fun getState(): PlayerState
    fun getPlayerReady()

    fun play()
    fun pause()
    fun stop()
}