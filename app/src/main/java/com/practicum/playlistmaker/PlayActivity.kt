package com.practicum.playlistmaker

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.util.AppPreferencesKeys
import com.practicum.playlistmaker.util.openThread
import com.practicum.playlistmaker.util.stopLoadingIndicator
import com.practicum.playlistmaker.util.toast
import kotlinx.serialization.json.Json
import timber.log.Timber

class PlayActivity : AppCompatActivity() {

//    private var track: Track? = null
    private lateinit var btnBackFromSettings: Button
    private lateinit var btnPlay: ImageView
    private lateinit var btnAddToPlaylist: ImageView
    private lateinit var btnLike: ImageView
    private var isPlaying: Boolean = false
    private var isAddedToPlaylist: Boolean = false
    private var isLiked: Boolean = false
    private var count =
        0 // заплатка на решение проблемы с появлением Тоста в момент прожатия кнопки
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()
    private var url: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        val trackJson = intent.getStringExtra("trackJson")
        val track = Json.decodeFromString(Track.serializer(), trackJson!!)
        // Текстово-активичное
        findViewById<TextView>(R.id.track_name).text = track.trackName
        findViewById<TextView>(R.id.artist_name).text = track.artistName
        findViewById<TextView>(R.id.track_time).text =
            formatTrackDuration(track.trackTimeMillis ?: 0)
        findViewById<TextView>(R.id.content1).text = formatTrackDuration(track.trackTimeMillis ?: 0)
        findViewById<TextView>(R.id.content2).text = track.collectionName
        findViewById<TextView>(R.id.content3).text = track.releaseDate
        findViewById<TextView>(R.id.content4).text = track.primaryGenreName
        findViewById<TextView>(R.id.content5).text = track.country
        url = track.previewUrl
        track.artworkUrl100?.replace("100x100bb.jpg", "512x512bb.jpg")?.let {
            loadImage(it, findViewById(R.id.track_cover))
        }
        // Кнопочно-активичное
        btnBackFromSettings = findViewById(R.id.button_back_from_settings)
        btnPlay = findViewById(R.id.btn_play)
        btnAddToPlaylist = findViewById(R.id.btn_add_to_playlist)
        btnLike = findViewById(R.id.btn_like)
        setupBackButton()
        setupAddToPlaylistButton()
        setupLikeButton()

        // Подготовка к работе обработчиков
        btnPlay.performClick()
        btnAddToPlaylist.performClick()
        btnLike.performClick()
        count += 1

        // Подготовка плеера
        preparePlayer()
        setupPlayButton()
    } // конец onCreate

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun setupPlayButton() {
        btnPlay.setOnClickListener {
            playbackControl()
        }
    }

    private fun preparePlayer() {
        openThread {
            try {
                Timber.d("=== preparePlayer начинаем в потоке: ${Thread.currentThread().name}")
                mediaPlayer.setDataSource(url)
                mediaPlayer.prepareAsync()
                mediaPlayer.setOnPreparedListener {
                    runOnUiThread {
                        btnPlay.isEnabled = true
                        playerState = STATE_PREPARED
                        Timber.d("=== OnPreparedListener в потоке: ${Thread.currentThread().name}")
                        stopLoadingIndicator()
                    }
                }
                mediaPlayer.setOnCompletionListener {
                    runOnUiThread {
                        btnPlay.setImageResource(R.drawable.ic_btn_play)
                        playerState = STATE_PREPARED
                        Timber.d("=== OnCompletionListener в потоке: ${Thread.currentThread().name}")
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Ошибка при подготовке плеера")
                runOnUiThread {
                    toast(getString(R.string.error500))
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

    private fun formatTrackDuration(trackTimeMillis: Long): String {
        val minutes = java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(trackTimeMillis)
        val seconds =
            java.util.concurrent.TimeUnit.MILLISECONDS.toSeconds(trackTimeMillis) - java.util.concurrent.TimeUnit.MINUTES.toSeconds(
                minutes
            )
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun loadImage(imageUrl: String, imageView: ImageView) {
        Glide.with(imageView).load(imageUrl).placeholder(R.drawable.ic_placeholder)
            .transform(RoundedCorners(AppPreferencesKeys.ALBUM_ROUNDED_CORNERS))
            .error(R.drawable.ic_placeholder)
            .into(imageView)
    }

    private fun setupBackButton() {
        btnBackFromSettings.setOnClickListener {
            finish()
        }
    }

    private fun setupAddToPlaylistButton() {
        btnAddToPlaylist.setOnClickListener {

            val newImageResource = if (isAddedToPlaylist) {
                count -= 1
                R.drawable.ic_btn_add_to_playlist
            } else {
                count += 1
                R.drawable.ic_btn_add_to_playlist_done
            }
            if (count == 2) {
                showSnackbar("Плейлист «BeSt SoNg EvEr!» создан")
            }
            btnAddToPlaylist.setImageResource(newImageResource)
            isAddedToPlaylist = !isAddedToPlaylist
        }
    }

    private fun setupLikeButton() {
        btnLike.setOnClickListener {
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