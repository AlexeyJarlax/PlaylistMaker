package com.practicum.playlistmaker.player.domain

class MediaPlayerInteractorImpl(private val mediaPlayer: MediaPlayerRepository) : MediaPlayerInteractor {

    override fun getState(): PlayerState {
        return mediaPlayer.getPlayerState()
    }

    override fun getPlaybackPosition(): Int {
        return mediaPlayer.getPlaybackPosition()
    }

    override fun getPlayerReady() {
        mediaPlayer.getPlayerReady()
    }

    override fun setDataSource(url: String) {
        mediaPlayer.setDataSource(url)
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