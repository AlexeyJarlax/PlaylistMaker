package com.practicum.playlistmaker.player.data

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.domain.MediaPlayerRepository
import com.practicum.playlistmaker.player.domain.PlayerState
import timber.log.Timber
import java.io.IOException

class MediaPlayerRepositoryImpl(private var mediaPlayer: MediaPlayer) : MediaPlayerRepository {

    private var playerState = PlayerState.INITIAL

    override fun setDataURL(url: String) {
        Timber.d("=== class MediaPlayerRepositoryImpl => setDataURL(url: String) $url")
        try {
            when (playerState) {
                PlayerState.INITIAL, PlayerState.ERROR -> {
                    mediaPlayer.reset()
                    mediaPlayer.setDataSource(url)
                    mediaPlayer.prepareAsync()
                    mediaPlayer.setOnCompletionListener { onPlayerCompletion() }
                    playerState = PlayerState.READY
                }
                PlayerState.PAUSED -> {
                    mediaPlayer.start()
                    playerState = PlayerState.PLAYING
                }
                PlayerState.PLAYING -> {
                    mediaPlayer.stop()
                    mediaPlayer.reset()
                    mediaPlayer.setDataSource(url)
                    mediaPlayer.prepareAsync()
                    mediaPlayer.setOnCompletionListener { onPlayerCompletion() }
                    playerState = PlayerState.READY
                }
                PlayerState.KILL -> {
                    mediaPlayer = MediaPlayer()
                    mediaPlayer.setDataSource(url)
                    mediaPlayer.prepareAsync()
                    mediaPlayer.setOnCompletionListener { onPlayerCompletion() }
                    playerState = PlayerState.READY
                }

                else -> {}
            }
        } catch (e: IOException) {
            Timber.e(e, "Error setting data source")
            playerState = PlayerState.ERROR
        }
    }

    override fun getPlayerState(): PlayerState {
        return playerState
    }

    override fun getPlaybackPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun getPlayerReady() {
        prepareMediaPlayer()
    }

    override fun play() {
        if (playerState != PlayerState.ERROR) {
            if (playerState == PlayerState.INITIAL) {
                prepareMediaPlayer()
            }
            mediaPlayer.start()
            playerState = PlayerState.PLAYING
        }
    }

    override fun pause() {
        mediaPlayer.pause()
        playerState = PlayerState.PAUSED
    }

    override fun destroy() {
        mediaPlayer.release()
        playerState = PlayerState.KILL
    }

    private fun prepareMediaPlayer() {
        try {
            mediaPlayer.prepare()
            playerState = PlayerState.READY
        } catch (e: Exception) {
            Timber.e(e, "Error preparing media player")
            playerState = PlayerState.ERROR
            mediaPlayer.stop()
        }
        Timber.d("=== class MediaPlayerRepositoryImpl => prepareMediaPlayer() $playerState")
    }

    private fun onPlayerCompletion() {
        playerState = PlayerState.READY
    }
}