package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior

import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayBinding
import com.practicum.playlistmaker.medialibrary.domain.db.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.ArtworkUrlLoader
import com.practicum.playlistmaker.player.domain.PlayerState
import com.practicum.playlistmaker.utils.AppPreferencesKeys
import com.practicum.playlistmaker.utils.AppPreferencesKeys.AN_INSTANCE_OF_THE_TRACK_CLASS
import com.practicum.playlistmaker.utils.DebounceExtension
import com.practicum.playlistmaker.utils.setDebouncedClickListener
import com.practicum.playlistmaker.utils.stopLoadingIndicator
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayFragment : Fragment() {

    private var _binding: FragmentPlayBinding? = null
    private val binding get() = _binding!!
    private lateinit var track: Track
    private val viewModel: PlayViewModel by viewModel()
    private var isAddedToPlaylist: Boolean = false
//    private lateinit var playlistsAdapter: PlaylistsSmallAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private val playlistsList = ArrayList<Playlist>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val trackFromArguments = arguments?.getSerializable(AppPreferencesKeys.AN_INSTANCE_OF_THE_TRACK_CLASS) as? Track

        if (trackFromArguments != null) {
            track = trackFromArguments
            track.previewUrl?.let { viewModel.setDataURL(track) }
            viewModel.screenState.observe(viewLifecycleOwner) { screenState ->
                setupScreenState(screenState)
            }
            setTrackData(track)
            setupBtnsAndClickListeners()
        } else {
            Log.d("=== LOG ===", "=== Track где-то потерялся")
        }
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
        binding.buttonBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.btnPlay.setDebouncedClickListener { viewModel.playBtnClick() }
        setupLikeButton()
        setupAddToPlaylistButton()
        if (isAdded) {
            val indicatorDelay = DebounceExtension(AppPreferencesKeys.ONE_SECOND) {
                stopLoadingIndicator()
            }
            indicatorDelay.debounce()
        }
    }

    private fun setupScreenState(screenState: ScreenState) {
        Log.d("=== LOG ===", "===  class PlayActivity => setupScreenState ${screenState}")

        when (screenState) {
            ScreenState.Initial -> {
                val playerState: PlayerState = viewModel.getState()
                setupPlayerState(playerState, 0)
            }

            is ScreenState.Error -> {
                setupPlayerState(screenState.playerState, 0)
            }

            is ScreenState.Ready -> {
                setupPlayerState(screenState.playerState, screenState.playbackPosition)
            }

            is ScreenState.Content -> {
                setupPlayerState(screenState.playerState, screenState.playbackPosition)
            }
        }
    }

    private fun setupPlayerState(playerState: PlayerState, playbackPosition: Int) {
        Log.d("=== LOG ===", "=== class PlayActivity => setupPlayerState ${playerState}")
        when (playerState) {
            PlayerState.INITIAL -> {
                Log.d("=== LOG ===", "=== PlayerState.INITIAL")
            }

            PlayerState.READY -> {
                Log.d("=== LOG ===", "=== PlayerState.READY")
                binding.btnPlay.setImageResource(R.drawable.ic_btn_play)
                binding.trackTime.text =
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(0)
            }

            PlayerState.PLAYING -> {
                Log.d("=== LOG ===", "=== PlayerState.PLAYING")
                binding.btnPlay.setImageResource(R.drawable.ic_btn_play_done)
                binding.trackTime.text =
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(playbackPosition)
            }

            PlayerState.PAUSED -> {
                Log.d("=== LOG ===", "=== PlayerState.PAUSED")
                binding.btnPlay.setImageResource(R.drawable.ic_btn_play)
                binding.trackTime.text =
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(playbackPosition)
            }

            PlayerState.KILL -> {
                Log.d("=== LOG ===", "=== PlayerState.KILL")
                binding.btnPlay.setImageResource(R.drawable.ic_btn_play)
            }

            PlayerState.ERROR -> {
                Log.d("=== LOG ===", "=== PlayerState.ERROR")
                binding.btnPlay.setImageResource(R.drawable.ic_error_notfound)
            }
        }
    }

    private fun setupAddToPlaylistButton() {
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
        viewModel.playlists.observe(viewLifecycleOwner) { list ->
            playlistsList.clear()
            playlistsList.addAll(list)
//            playlistsAdapter.notifyDataSetChanged()
        }

        binding.btnAddToPlaylist.setDebouncedClickListener {
            findNavController().navigate(R.id.action_playFragment_to_MLCreatePlaylistFragment)
        }

        viewModel.addResult.observe(viewLifecycleOwner) { addResult ->
            if (addResult.successful==true) {
                showSnackbar("Плейлист создан")
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
            else if (addResult.successful==false) {
                showSnackbar("Плейлист вроде бы уже есть?")
            }
        }

        binding.btnAddToPlaylist.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
    }

    private fun setupLikeButton() {
        viewModel.isFavoriteTrack.observe(viewLifecycleOwner) { isFavoriteTrack ->
            if (isFavoriteTrack) binding.btnLike.setImageResource(R.drawable.ic_btn_like_done)
            else binding.btnLike.setImageResource(R.drawable.ic_btn_dont_like)
        }
        binding.btnLike.setDebouncedClickListener {
            viewModel.upsertFavoriteTrack(track)
            Log.d("=== LOG ===", "=== PlayFragment > setupLikeButton()")
        }
    }

    private fun initializeBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.lrBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior.addBottomSheetCallback(
            object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            binding.overlay.visibility = View.GONE
                        }

                        else -> {
                            binding.overlay.visibility = View.VISIBLE
                        }
                    }
                }
            }
        )
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun createArguments(track: Track): Bundle = bundleOf(AN_INSTANCE_OF_THE_TRACK_CLASS to track)
    }
}