package com.practicum.playlistmaker.player.data

import android.media.MediaPlayer
import android.util.Log
import com.practicum.playlistmaker.player.domain.MediaPlayerRepository
import com.practicum.playlistmaker.player.domain.PlayerState
import java.io.IOException

class MediaPlayerRepositoryImpl(private var mediaPlayer: MediaPlayer) : MediaPlayerRepository {

    private var playerState = PlayerState.INITIAL
    private val mediaPlayerLock = Any()

    override fun setDataURL(url: String) {
        synchronized(mediaPlayerLock) {
            Log.d("=== LOG ===", "=== class MediaPlayerRepositoryImpl => setDataURL(url: String) $url")
            resetPlayer()
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
                        mediaPlayer.release()
                        mediaPlayer = MediaPlayer()
                        mediaPlayer.setDataSource(url)
                        mediaPlayer.prepareAsync()
                        mediaPlayer.setOnCompletionListener { onPlayerCompletion() }
                        playerState = PlayerState.READY
                    }
                    else -> {}
                }
            } catch (e: IOException) {
                Log.e("=== LOG ===", "=== class MediaPlayerRepositoryImpl => Error setting data source", e)
                playerState = PlayerState.ERROR
            }
        }
    }

    override fun getPlayerState(): PlayerState {
        synchronized(mediaPlayerLock) {
            return playerState
        }
    }

    override fun getPlaybackPosition(): Int {
        synchronized(mediaPlayerLock) {
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
    }

    override fun getPlayerReady() {
        synchronized(mediaPlayerLock) {
            prepareMediaPlayer()
        }
    }

    override fun play() {
        synchronized(mediaPlayerLock) {
            if (playerState != PlayerState.ERROR) {
                if (playerState == PlayerState.INITIAL) {
                    prepareMediaPlayer()
                }
                mediaPlayer.start()
                playerState = PlayerState.PLAYING
            }
        }
    }

    override fun pause() {
        synchronized(mediaPlayerLock) {
            mediaPlayer.pause()
            playerState = PlayerState.PAUSED
        }
    }

    override fun destroy() {
        synchronized(mediaPlayerLock) {
            mediaPlayer.release()
            playerState = PlayerState.KILL
        }
    }

    private fun prepareMediaPlayer() {
        synchronized(mediaPlayerLock) {
            try {
                mediaPlayer.prepare()
                playerState = PlayerState.READY
            } catch (e: Exception) {
                Log.e("=== LOG ===", "=== class MediaPlayerRepositoryImpl => Error preparing media player", e)
                playerState = PlayerState.ERROR
                mediaPlayer.stop()
            }
            Log.d("=== LOG ===", "=== class MediaPlayerRepositoryImpl => prepareMediaPlayer() $playerState")
        }
    }

    private fun onPlayerCompletion() {
        synchronized(mediaPlayerLock) {
            playerState = PlayerState.READY
        }
    }

    override fun resetPlayer() {
        synchronized(mediaPlayerLock) {
            try {
                if (playerState != PlayerState.INITIAL) {
                    mediaPlayer.stop()
                    mediaPlayer.reset()
                    playerState = PlayerState.INITIAL
                }
            } catch (e: Exception) {
                Log.e("=== LOG ===", "=== Error resetting media player", e)
                playerState = PlayerState.ERROR
            }
        }
    }
}