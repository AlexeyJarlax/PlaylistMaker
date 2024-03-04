package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar

import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayBinding
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.ArtworkUrlLoader
import com.practicum.playlistmaker.player.domain.PlayerState
import com.practicum.playlistmaker.utils.AppPreferencesKeys
import com.practicum.playlistmaker.utils.DebounceExtension
import com.practicum.playlistmaker.utils.buttonToGoBack
import com.practicum.playlistmaker.utils.setDebouncedClickListener
import com.practicum.playlistmaker.utils.startLoadingIndicator
import com.practicum.playlistmaker.utils.stopLoadingIndicator
import timber.log.Timber

class PlayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayBinding
    private lateinit var track: Track
    private lateinit var viewModel: PlayViewModel
    private lateinit var secondsCounter: SecondsCounter
    private var isAddedToPlaylist: Boolean = false
    private var isLiked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track =
            (intent?.getSerializableExtra(AppPreferencesKeys.AN_INSTANCE_OF_THE_TRACK_CLASS) as? Track)!!
        viewModel = ViewModelProvider(
            this,
            PlayViewModel.getViewModelFactory(track.previewUrl)
        )[PlayViewModel::class.java]
        viewModel.screenState.observe(this@PlayActivity) { screenState ->
            setupScreenState(screenState)
        }
        secondsCounter = SecondsCounter({ elapsedTime -> }, binding)
        setTrackData(track)
        setupBtnsAndClickListeners()
    }

    private fun setTrackData(track: Track) {
        with(binding) {
            trackName.text = track.trackName
            artistName.text = track.artistName
            contentDuration.text = track.trackTime
            contentAlbum.text = track.collectionName
            contentYear.text = track.releaseYear
            contentGenre.text = track.primaryGenreName
            contentCountry.text = track.country
        }
        binding.trackTime.text = getString(R.string.zero_time)
        ArtworkUrlLoader().loadImage(
            track.artworkUrl100?.replace("100x100bb.jpg", "512x512bb.jpg"),
            binding.trackCover
        )
    }

    private fun setupBtnsAndClickListeners() {
        startLoadingIndicator()
        buttonToGoBack()
        binding.btnPlay.setDebouncedClickListener { viewModel.playBtnClick() }
        setupLikeButton()
        setupAddToPlaylistButton()
        val indicatorDelay = DebounceExtension(AppPreferencesKeys.TWO_SECONDS) {stopLoadingIndicator()}
        indicatorDelay.debounce()
    }

    private fun setupScreenState(screenState: ScreenState) {
            when (screenState) {
            ScreenState.Error -> {
                setupPlayerState(PlayerState.ERROR)
            }
            ScreenState.Initial -> {
                setupPlayerState(PlayerState.INITIAL)
            }
            is ScreenState.Ready -> {
                setupPlayerState(PlayerState.READY)
            }
            is ScreenState.Content -> {
                setupPlayerState(screenState.playerState)
            }
        }
    }

    private fun setupPlayerState(playerState: PlayerState) {
        when (playerState) {
            PlayerState.INITIAL -> {
                Timber.d("PlayerState.INITIAL")
            }
            PlayerState.READY -> {
                Timber.d("PlayerState.READY")
            }
            PlayerState.PLAYING -> {
                Timber.d("PlayerState.PLAYING")
                binding.btnPlay.setImageResource(R.drawable.ic_btn_play_done)
                secondsCounter.start()
            }
            PlayerState.PAUSED -> {
                Timber.d("PlayerState.PAUSED")
                binding.btnPlay.setImageResource(R.drawable.ic_btn_play)
                secondsCounter.stop()
            }
            PlayerState.KILL -> {
                Timber.d("PlayerState.KILL")
                binding.btnPlay.setImageResource(R.drawable.ic_btn_play)
            }
            PlayerState.ERROR -> {
                Timber.d("PlayerState.ERROR")
                binding.btnPlay.setImageResource(R.drawable.ic_error_notfound)
            }
        }
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

    override fun onPause() {
        super.onPause()
        viewModel.onActivityPaused()
        secondsCounter.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        secondsCounter.reset()
        secondsCounter.stop()
    }
}