package com.practicum.playlistmaker.player.data

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.domain.MediaPlayerData
import com.practicum.playlistmaker.player.domain.PlayerState

class MediaPlayerDataImpl(url: String) : MediaPlayerData {

        private var mediaPlayer = MediaPlayer()
        private var playerState = PlayerState.INITIAL

        init {
            mediaPlayer.setDataSource(url)
            mediaPlayer.setOnCompletionListener { onPlayerCompletion() }
        }

        override fun getPlayerState(): PlayerState {
            return playerState
        }

        override fun getPlaybackPosition(): Int {
            return mediaPlayer.currentPosition
        }

    override fun getPlayerReady() {
        playerState = PlayerState.READY
        prepareMediaPlayer()
    }

        override fun play() {
            if (playerState == PlayerState.INITIAL) {
                prepareMediaPlayer()
            }
            mediaPlayer.start()
            playerState = PlayerState.PLAYING
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
                playerState = PlayerState.ERROR
            }
        }

        private fun onPlayerCompletion() {
            playerState = PlayerState.READY
        }
    }