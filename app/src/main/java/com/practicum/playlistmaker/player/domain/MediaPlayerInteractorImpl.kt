package com.practicum.playlistmaker.player.domain

class MediaPlayerInteractorImpl(private val mediaPlayer: MediaPlayerData) : MediaPlayerInteractor {
    override fun getState(): PlayerState {
        return mediaPlayer.getPlayerState()
    }

    override fun getPlayerReady() {
        mediaPlayer.getPlayerReady()
    }

    override fun play() {
        mediaPlayer.play()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun stop() {
        mediaPlayer.destroy()
    }
}