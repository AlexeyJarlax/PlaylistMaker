package com.practicum.playlistmaker.player.domain

interface MediaPlayerData {
    fun getPlayerState(): PlayerState
    fun getPlaybackPosition(): Int
    fun getPlayerReady()

    fun play()
    fun pause()
    fun destroy()
}