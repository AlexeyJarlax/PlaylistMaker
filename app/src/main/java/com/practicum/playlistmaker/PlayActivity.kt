package com.practicum.playlistmaker

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.util.AppPreferencesKeys
import com.practicum.playlistmaker.util.SecondsCounter
import com.practicum.playlistmaker.util.setDebouncedClickListener
import com.practicum.playlistmaker.util.startLoadingIndicator
import com.practicum.playlistmaker.util.stopLoadingIndicator
import com.practicum.playlistmaker.util.toast
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
        findViewById<TextView>(R.id.track_name).text = track.trackName
        findViewById<TextView>(R.id.artist_name).text = track.artistName
        findViewById<TextView>(R.id.content1).text = formatTrackDuration(track.trackTimeMillis ?: 0)
        findViewById<TextView>(R.id.content2).text = track.collectionName
        findViewById<TextView>(R.id.content3).text = track.releaseDate
        findViewById<TextView>(R.id.content4).text = track.primaryGenreName
        findViewById<TextView>(R.id.content5).text = track.country
        track.artworkUrl100?.replace("100x100bb.jpg", "512x512bb.jpg")?.let {
            loadImage(it, findViewById(R.id.track_cover))
        }
        trackTime = findViewById(R.id.track_time)
        trackTime.text = formatTrackDuration(track.trackTimeMillis ?: 0)
        btnBackFromSettings = findViewById(R.id.button_back_from_settings)
        btnPlay = findViewById(R.id.btn_play)
        btnAddToPlaylist = findViewById(R.id.btn_add_to_playlist)
        btnLike = findViewById(R.id.btn_like)

        secondsCounter = SecondsCounter { seconds ->
            trackTime.text = formatTrackDuration(seconds * 1000)
        }
        setupBackButton()
        setupAddToPlaylistButton()
        setupLikeButton()
        preparePlayer()
        setupPlayButton()
    } // конец onCreate

    override fun onPause() {
        super.onPause()
        pausePlayer()
        secondsCounter.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        secondsCounter.reset()
        secondsCounter.stop()
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
                secondsCounter.handlerRepeater { // цикл повторов с обновлением индикатора времени
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
            trackTime.text = formattedTime
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

    private fun formatTrackDuration(duration: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
        val seconds =
            TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(minutes)
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }

    private fun preparePlayer() {
        startLoadingIndicator()
        try {
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                runOnUiThread {
                    btnPlay.isEnabled = true
                    playerState = STATE_PREPARED
                    stopLoadingIndicator()
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
                toast(getString(R.string.error500))
                stopLoadingIndicator()
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

    private fun loadImage(imageUrl: String, imageView: ImageView) {
        Glide.with(imageView).load(imageUrl).placeholder(R.drawable.ic_placeholder)
            .transform(RoundedCorners(AppPreferencesKeys.ALBUM_ROUNDED_CORNERS))
            .error(R.drawable.ic_placeholder)
            .into(imageView)
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