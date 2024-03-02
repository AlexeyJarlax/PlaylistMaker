package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayBinding
import com.practicum.playlistmaker.player.SecondsCounter
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.setDebouncedClickListener
import com.practicum.playlistmaker.utils.ArtworkUrlLoader
import com.practicum.playlistmaker.utils.buttonToGoBack
import kotlinx.serialization.json.Json
//
class PlayerActivity : FunctionsForPlayActivity() { //FunctionsForPlayActivity содержит функции проигрывания, PlayActivity - остальные функции вьюхи
//
//    private lateinit var trackUseCase: RepositoryForSelectedTrack  // TrackUseCase интерфейс
//    private var isAddedToPlaylist: Boolean = false
//    private var isLiked: Boolean = false
//    private var url: String? = null
//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        trackUseCase = (application as ProviderForSelectedTrack).provideTrackUseCase() // TrackUseCase интерфейс
//        val trackJson = intent.getStringExtra("trackJson")
//        val track = Json.decodeFromString(TracksList.serializer(), trackJson!!)
//        url = track.previewUrl
//        trackUseCase = provideTrackUseCase()
//        ArtworkUrlLoader().loadImage(track.artworkUrl100?.replace("100x100bb.jpg", "512x512bb.jpg"), binding.trackCover)
//        bindingView(track)
//        setupAddToPlaylistButton()
//        setupLikeButton()
//        url?.let { preparePlayer(it) }
//        setupPlayButton()
//        buttonToGoBack()
    }
//
    private fun bindingView(track: Track) {
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
//        binding.contentDuration.text = formatTrackDuration(track.trackTimeMillis ?: 0)
        binding.contentAlbum.text = track.collectionName
        binding.contentYear.text = track.releaseDate
        binding.contentGenre.text = track.primaryGenreName
        binding.contentCountry.text = track.country
//        binding.trackTime.text = formatTrackDuration(track.trackTimeMillis ?: 0)
        secondsCounter = SecondsCounter { seconds ->
        binding.trackTime.text = formatTrackDuration(seconds * 1000)
        }
    }}
//
//    override fun provideTrackUseCase(): RepositoryForSelectedTrack {  // TrackUseCase интерфейс
//        return trackUseCase
//    }
//
//    override fun onPause() {
//        super.onPause()
//        pausePlayer()
//        secondsCounter.stop()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        mediaPlayer.release()
//        secondsCounter.reset()
//        secondsCounter.stop()
//    }
//
//    private fun setupPlayButton() {
//        binding.btnPlay.setDebouncedClickListener {
//            playbackControl()
//            if (isPlaying) {
//                secondsCounter.start()
//            } else {
//                secondsCounter.stop()
//            }
//        }
//    }
//
//    private fun setupAddToPlaylistButton() {
//        binding.btnAddToPlaylist.setDebouncedClickListener {
//            val newImageResource = if (isAddedToPlaylist) {
//                R.drawable.ic_btn_add_to_playlist
//            } else {
//                showSnackbar("Плейлист «BeSt SoNg EvEr!» создан")
//                R.drawable.ic_btn_add_to_playlist_done
//            }
//            binding.btnAddToPlaylist.setImageResource(newImageResource)
//            isAddedToPlaylist = !isAddedToPlaylist
//        }
//    }
//
//    private fun setupLikeButton() {
//        binding.btnLike.setDebouncedClickListener {
//            val newImageResource = if (isLiked) {
//                R.drawable.ic_btn_like
//            } else {
//                R.drawable.ic_btn_like_done
//            }
//            binding.btnLike.setImageResource(newImageResource)
//            isLiked = !isLiked
//        }
//    }
//
//    private fun showSnackbar(message: String) {
//        val snackbar = Snackbar.make(
//            binding.titleYear,
//            message,
//            Snackbar.LENGTH_SHORT
//        )
//        val snackbarView = snackbar.view
//        snackbarView.setBackgroundResource(R.color.yp_black_and_yp_white)
//        snackbar.show()
//    }
