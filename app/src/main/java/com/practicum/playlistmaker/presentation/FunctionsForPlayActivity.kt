package com.practicum.playlistmaker.presentation

//функции проигрывания

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayBinding
import com.practicum.playlistmaker.domain.api.ProviderForSelectedTrack
import java.util.Locale
import java.util.concurrent.TimeUnit

abstract class FunctionsForPlayActivity : AppCompatActivity(), ProviderForSelectedTrack {

    lateinit var secondsCounter: SecondsCounter
    lateinit var mediaPlayer: MediaPlayer
    lateinit var binding: ActivityPlayBinding
    var playerState: Int = STATE_DEFAULT
    var isPlaying: Boolean = false

     companion object {
         private const val STATE_DEFAULT = 0
         private const val STATE_PREPARED = 1
         private const val STATE_PLAYING = 2
         private const val STATE_PAUSED = 3
     }

    fun playbackControl() {
         when (playerState) {
             STATE_PLAYING -> {
                 pausePlayer()
             }
             STATE_PREPARED, STATE_PAUSED -> {
                 startPlayer()
                 secondsCounter.handlerRepeater {
                     if (playerState == STATE_PLAYING) {
                         updatePlaybackTime(mediaPlayer.currentPosition.toLong())
                     }
                 }
             }
         }
     }

     private fun updatePlaybackTime(duration: Long) {
         if (mediaPlayer.isPlaying) {
             val formattedTime = formatTrackDuration(duration)
             binding.trackTime.text = formattedTime
         }
     }


     fun formatTrackDuration(duration: Long): String {
         val minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
         val seconds =
             TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(minutes)
         return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
     }

     fun preparePlayer(url: String) {
         startLoadingIndicator()
         try {
             mediaPlayer = MediaPlayer()
             mediaPlayer.setDataSource(url)
             mediaPlayer.prepareAsync()
             mediaPlayer.setOnPreparedListener {
                 runOnUiThread {
                     binding.btnPlay.isEnabled = true
                     playerState = STATE_PREPARED
                     stopLoadingIndicator()
                 }
             }
             mediaPlayer.setOnCompletionListener {
                 runOnUiThread {
                     binding.btnPlay.setImageResource(R.drawable.ic_btn_play)
                     playerState = STATE_PREPARED
                 }
             }
             mediaPlayer.setOnErrorListener { mp, what, extra ->
                 runOnUiThread {
                     stopLoadingIndicator()
                 }
                 false
             }
         } catch (e: Exception) {
             runOnUiThread {
                 toast(getString(R.string.error500))
                 stopLoadingIndicator()
             }
         }
     }


     private fun startPlayer() {
         mediaPlayer.start()
         binding.btnPlay.setImageResource(R.drawable.ic_btn_play_done)
         playerState = STATE_PLAYING
     }

     fun pausePlayer() {
         mediaPlayer.pause()
         binding.btnPlay.setImageResource(R.drawable.ic_btn_play)
         playerState = STATE_PAUSED
     }
 }