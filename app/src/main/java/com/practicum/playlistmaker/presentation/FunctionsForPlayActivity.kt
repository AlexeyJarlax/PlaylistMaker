package com.practicum.playlistmaker.presentation

import android.media.MediaPlayer
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.preferences.AppPreferencesKeys
import com.practicum.playlistmaker.databinding.ActivityPlayBinding
import com.practicum.playlistmaker.domain.api.RepositoryForSelectedTrack
import com.practicum.playlistmaker.domain.impl.SecondsCounter
import com.practicum.playlistmaker.presentation.startLoadingIndicator
import com.practicum.playlistmaker.presentation.stopLoadingIndicator
import com.practicum.playlistmaker.presentation.toast
import com.practicum.playlistmaker.ui.PlayActivity
import java.util.Locale
import java.util.concurrent.TimeUnit

 open class FunctionsForPlayActivity {
//     private lateinit var trackUseCase: RepositoryForSelectedTrack  // TrackUseCase интерфейс
     private lateinit var binding: ActivityPlayBinding
     private lateinit var mediaPlayer: MediaPlayer
     private lateinit var secondsCounter: SecondsCounter

     private var isPlaying: Boolean = false
     private var isAddedToPlaylist: Boolean = false
     private var isLiked: Boolean = false
     private var playerState = STATE_DEFAULT
     private var url: String? = null

     companion object {
         private const val STATE_DEFAULT = 0
         private const val STATE_PREPARED = 1
         private const val STATE_PLAYING = 2
         private const val STATE_PAUSED = 3
     }

    private fun playbackControl() {
        when (playerState) {
            FunctionsForPlayActivity.STATE_PLAYING -> {
                pausePlayer()
            }

            FunctionsForPlayActivity.STATE_PREPARED, FunctionsForPlayActivity.STATE_PAUSED -> {
                startPlayer()
                secondsCounter.handlerRepeater {
                    if (playerState == FunctionsForPlayActivity.STATE_PLAYING) {
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

//    fun setupPlayButton() {
//        binding.btnPlay.setDebouncedClickListener {
//            playbackControl()
//
//            if (isPlaying) {
//                secondsCounter.start()
//            } else {
//                secondsCounter.stop()
//            }
//        }
//    }

    fun formatTrackDuration(duration: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
        val seconds =
            TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(minutes)
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }

    fun preparePlayer() {
        startLoadingIndicator()
        try {
            mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                runOnUiThread {
                    binding.btnPlay.isEnabled = true
                    playerState = PlayActivity.STATE_PREPARED
                    stopLoadingIndicator()
                }
            }
            mediaPlayer.setOnCompletionListener {
                runOnUiThread {
                    binding.btnPlay.setImageResource(R.drawable.ic_btn_play)
                    playerState = PlayActivity.STATE_PREPARED
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

//    private fun startPlayer() {
//        mediaPlayer.start()
//        binding.btnPlay.setImageResource(R.drawable.ic_btn_play_done)
//        playerState = PlayActivity.STATE_PLAYING
//    }
//
//    fun pausePlayer() {
//        mediaPlayer.pause()
//        binding.btnPlay.setImageResource(R.drawable.ic_btn_play)
//        playerState = PlayActivity.STATE_PAUSED
//    }

    fun loadImage(imageUrl: String?, imageView: ImageView) {
        Glide.with(imageView).load(imageUrl).placeholder(R.drawable.ic_placeholder)
            .transform(RoundedCorners(AppPreferencesKeys.ALBUM_ROUNDED_CORNERS))
            .error(R.drawable.ic_placeholder)
            .into(imageView)
    }
}