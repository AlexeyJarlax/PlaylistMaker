package com.practicum.playlistmaker.player.data

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.domain.MediaPlayerRepository
import com.practicum.playlistmaker.player.domain.PlayerState
import com.practicum.playlistmaker.player.ui.ScreenState
import timber.log.Timber

class MediaPlayerRepositoryImpl(url: String) : MediaPlayerRepository {

    private val mediaPlayer = MediaPlayer()
    private var playerState = PlayerState.INITIAL

    init {
        mediaPlayer.setDataSource(url)
//            mediaPlayer.setOnCompletionListener { onPlayerCompletion() }
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
        } catch (e: Exception) {
            playerState = PlayerState.ERROR
        } finally {
            if (playerState == PlayerState.ERROR) {
                mediaPlayer.stop()
            } else {
                playerState = PlayerState.READY
            }
            Timber.d("=== class MediaPlayerRepositoryImpl => prepareMediaPlayer() ${playerState}")
        }

    }

//        private fun onPlayerCompletion() {
//            playerState = PlayerState.READY
//        }
}