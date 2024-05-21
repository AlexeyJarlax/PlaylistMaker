package com.practicum.playlistmaker.player.data

import android.media.MediaPlayer
import android.util.Log
import com.practicum.playlistmaker.player.domain.MediaPlayerRepository
import com.practicum.playlistmaker.player.domain.PlayerState
import java.io.IOException

class MediaPlayerRepositoryImpl(private var mediaPlayer: MediaPlayer) : MediaPlayerRepository {

    private var playerState = PlayerState.INITIAL

    override fun setDataURL(url: String) {
        Log.d("=== LOG ===", "=== class MediaPlayerRepositoryImpl => setDataURL(url: String) $url")
        resetPlayer()
        try {
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnCompletionListener { onPlayerCompletion() }
            playerState = PlayerState.READY
        } catch (e: IOException) {
            Log.e("=== LOG ===", "=== class MediaPlayerRepositoryImpl => Error setting data source")
            playerState = PlayerState.ERROR
        }
    }

    override fun getPlayerState(): PlayerState {
        return playerState
    }

    override fun getPlaybackPosition(): Int {
        return try {
            if (mediaPlayer.isPlaying || mediaPlayer.currentPosition >= 0) {
                mediaPlayer.currentPosition
            } else {
                0
            }
        } catch (e: IllegalStateException) {
            Log.e("=== LOG ===", "=== MediaPlayerRepositoryImpl > getPlaybackPosition()", e)
            0
        }
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
            Log.e("=== LOG ===", "=== class MediaPlayerRepositoryImpl => Error preparing media player")
            playerState = PlayerState.ERROR
            mediaPlayer.stop()
        }
        Log.d("=== LOG ===", "=== class MediaPlayerRepositoryImpl => prepareMediaPlayer() $playerState")
    }

    private fun onPlayerCompletion() {
        playerState = PlayerState.READY
    }

    override fun resetPlayer() {
        try {
            if (playerState != PlayerState.INITIAL) {
                mediaPlayer.stop()
                mediaPlayer.reset()
                playerState = PlayerState.INITIAL
            }
        } catch (e: Exception) {
            Log.e("=== LOG ===", "Error resetting media player", e)
            playerState = PlayerState.ERROR
        }
    }
}
