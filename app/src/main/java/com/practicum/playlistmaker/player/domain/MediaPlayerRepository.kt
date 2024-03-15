package com.practicum.playlistmaker.player.domain

interface MediaPlayerRepository {
    fun getPlayerState(): PlayerState
    fun getPlaybackPosition(): Int
    fun getPlayerReady()
    fun setDataSource(url: String)
    fun play()
    fun pause()
    fun destroy()
}