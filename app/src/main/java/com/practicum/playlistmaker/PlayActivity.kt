package com.practicum.playlistmaker

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.util.DebounceExtension
import com.practicum.playlistmaker.util.SecondsCounter
import com.practicum.playlistmaker.util.setDebouncedClickListener
import kotlinx.serialization.json.Json
import java.util.Locale
import java.util.concurrent.TimeUnit


class PlayActivity : AppCompatActivity() {

    private lateinit var btnBackFromSettings: Button
    private lateinit var btnPlay: ImageView
    private lateinit var btnAddToPlaylist: ImageView
    private lateinit var btnLike: ImageView
    private var isPlaying: Boolean = false
    private var isAddedToPlaylist: Boolean = false
    private var isLiked: Boolean = false
    private lateinit var trackTime: TextView

    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()
    private var url: String? = null
    private val debouncer = DebounceExtension(1000) {
        updatePlaybackTime()
    }
    private val handler = debouncer.getHandler()
    private lateinit var secondsCounter: SecondsCounter

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        val trackJson = intent.getStringExtra("trackJson")
        val track = Json.decodeFromString(Track.serializer(), trackJson!!)
        url = track.previewUrl

        trackTime = findViewById(R.id.track_time)
        btnBackFromSettings = findViewById(R.id.button_back_from_settings)
        btnPlay = findViewById(R.id.btn_play)
        btnAddToPlaylist = findViewById(R.id.btn_add_to_playlist)
        btnLike = findViewById(R.id.btn_like)

        secondsCounter = SecondsCounter { seconds ->
            trackTime.text = formatTrackDuration(seconds * 1000)
        }

        preparePlayer()
        setupPlayButton()
        setupBackButton()
        setupAddToPlaylistButton()
        setupLikeButton()
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
        secondsCounter.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateProgressRunnable)
        mediaPlayer.release()
        secondsCounter.stop()
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
                updatePlaybackTime()
            }
        }
    }

    private fun setupPlayButton() {
        btnPlay.setDebouncedClickListener {
            playbackControl()

            if (isPlaying) {
                secondsCounter.start()
            } else {
                secondsCounter.stop()
            }
        }
    }

    private fun updatePlaybackTime() {
        if (mediaPlayer != null && (mediaPlayer.isPlaying || mediaPlayer.duration > 0)) {
            val totalDuration = mediaPlayer.duration
            val currentPosition = mediaPlayer.currentPosition
            val formattedTime: String = if (mediaPlayer.isPlaying) {
                formatTrackDuration(currentPosition.toLong())
            } else {
                formatTrackDuration(totalDuration.toLong())
            }
            trackTime.text = formattedTime
        }
    }

    private fun formatTrackDuration(duration: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(minutes)
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }

    private fun preparePlayer() {
        openThread {
            try {
                mediaPlayer.setDataSource(url)
                mediaPlayer.prepareAsync()
                mediaPlayer.setOnPreparedListener {
                    runOnUiThread {
                        btnPlay.isEnabled = true
                        playerState = STATE_PREPARED
                        updatePlaybackTime()
                        handler.post(updateProgressRunnable)
                    }
                }
                mediaPlayer.setOnCompletionListener {
                    runOnUiThread {
                        btnPlay.setImageResource(R.drawable.ic_btn_play)
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
                    stopLoadingIndicator()
                }
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        btnPlay.setImageResource(R.drawable.ic_btn_play_done)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        btnPlay.setImageResource(R.drawable.ic_btn_play)
        playerState = STATE_PAUSED
    }

    private val updateProgressRunnable = object : Runnable {
        override fun run() {
            debouncer.debounce()
            handler.postDelayed(this, 1000)
        }
    }

    private fun stopLoadingIndicator() {
        // Implementation for stopLoadingIndicator method...
    }

    private fun openThread(block: () -> Unit) {
        // Implementation for openThread method...
    }

    private fun setupBackButton() {
        btnBackFromSettings.setDebouncedClickListener {
            finish()
        }
    }

    private fun setupAddToPlaylistButton() {
        btnAddToPlaylist.setDebouncedClickListener {
            val newImageResource = if (isAddedToPlaylist) {
                R.drawable.ic_btn_add_to_playlist
            } else {
                showSnackbar("Плейлист «BeSt SoNg EvEr!» создан")
                R.drawable.ic_btn_add_to_playlist_done
            }
            btnAddToPlaylist.setImageResource(newImageResource)
            isAddedToPlaylist = !isAddedToPlaylist
        }
    }

    private fun setupLikeButton() {
        btnLike.setDebouncedClickListener {
            val newImageResource = if (isLiked) {
                R.drawable.ic_btn_like_done
            } else {
                R.drawable.ic_btn_like
            }
            btnLike.setImageResource(newImageResource)
            isLiked = !isLiked
        }
    }

    private fun showSnackbar(message: String) {
        val snackbar = Snackbar.make(
            findViewById(R.id.title3),
            message,
            Snackbar.LENGTH_SHORT
        )
        val snackbarView = snackbar.view
        snackbarView.setBackgroundResource(R.color.yp_black_and_yp_white)
        snackbar.show()
    }
}