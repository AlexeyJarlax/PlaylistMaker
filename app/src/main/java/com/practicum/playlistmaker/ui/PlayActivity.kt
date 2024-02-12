package com.practicum.playlistmaker.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.media.MediaPlayer
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.preferences.AppPreferencesKeys
import com.practicum.playlistmaker.databinding.ActivityPlayBinding
import com.practicum.playlistmaker.domain.api.TrackUseCase
import com.practicum.playlistmaker.domain.api.TrackUseCaseProvider
import com.practicum.playlistmaker.domain.impl.SecondsCounter
import com.practicum.playlistmaker.domain.impl.setDebouncedClickListener
import com.practicum.playlistmaker.domain.models.TracksList
import com.practicum.playlistmaker.presentation.buttonBack
import com.practicum.playlistmaker.presentation.startLoadingIndicator
import com.practicum.playlistmaker.presentation.stopLoadingIndicator
import com.practicum.playlistmaker.presentation.toast
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.util.Locale
import java.util.concurrent.TimeUnit

class PlayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayBinding
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var secondsCounter: SecondsCounter
    private lateinit var trackUseCase: TrackUseCase

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Получение TrackUseCase через интерфейс
        trackUseCase = (application as TrackUseCaseProvider).provideTrackUseCase()

        val trackJson = intent.getStringExtra("trackJson")
        val track = Json.decodeFromString(TracksList.serializer(), trackJson!!)
        url = track.previewUrl

        loadImage(track.artworkUrl100?.replace("100x100bb.jpg", "512x512bb.jpg"), binding.trackCover)
        bindingView(track)
        setupAddToPlaylistButton()
        setupLikeButton()
        preparePlayer()
        setupPlayButton()
        buttonBack()
        }


    private fun bindingView(track: TracksList) {
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.contentDuration.text = formatTrackDuration(track.trackTimeMillis ?: 0)
        binding.contentAlbum.text = track.collectionName
        binding.contentYear.text = track.releaseDate
        binding.contentGenre.text = track.primaryGenreName
        binding.contentCountry.text = track.country
        binding.trackTime.text = formatTrackDuration(track.trackTimeMillis ?: 0)
        secondsCounter = SecondsCounter { seconds ->
            binding.trackTime.text = formatTrackDuration(seconds * 1000)
        }
    }

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

    private fun setupPlayButton() {
        binding.btnPlay.setDebouncedClickListener {
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

    private fun pausePlayer() {
        mediaPlayer.pause()
        binding.btnPlay.setImageResource(R.drawable.ic_btn_play)
        playerState = STATE_PAUSED
    }

    private fun loadImage(imageUrl: String?, imageView: ImageView) {
        Glide.with(imageView).load(imageUrl).placeholder(R.drawable.ic_placeholder)
            .transform(RoundedCorners(AppPreferencesKeys.ALBUM_ROUNDED_CORNERS))
            .error(R.drawable.ic_placeholder)
            .into(imageView)
    }

    private fun setupAddToPlaylistButton() {
        binding.btnAddToPlaylist.setDebouncedClickListener {
            val newImageResource = if (isAddedToPlaylist) {
                R.drawable.ic_btn_add_to_playlist
            } else {
                showSnackbar("Плейлист «BeSt SoNg EvEr!» создан")
                R.drawable.ic_btn_add_to_playlist_done
            }
            binding.btnAddToPlaylist.setImageResource(newImageResource)
            isAddedToPlaylist = !isAddedToPlaylist
        }
    }

    private fun setupLikeButton() {
        binding.btnLike.setDebouncedClickListener {
            val newImageResource = if (isLiked) {
                R.drawable.ic_btn_like
            } else {
                R.drawable.ic_btn_like_done
            }
            binding.btnLike.setImageResource(newImageResource)
            isLiked = !isLiked
        }
    }

    private fun showSnackbar(message: String) {
        val snackbar = Snackbar.make(
            binding.titleYear,
            message,
            Snackbar.LENGTH_SHORT
        )
        val snackbarView = snackbar.view
        snackbarView.setBackgroundResource(R.color.yp_black_and_yp_white)
        snackbar.show()
    }
}