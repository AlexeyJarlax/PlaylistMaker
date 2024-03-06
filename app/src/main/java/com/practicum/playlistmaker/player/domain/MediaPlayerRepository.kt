package com.practicum.playlistmaker.player.domain

interface MediaPlayerRepository {
    fun getPlayerState(): PlayerState
    fun getPlaybackPosition(): Int
    fun getPlayerReady()

    fun play()
    fun pause()
    fun destroy()
}