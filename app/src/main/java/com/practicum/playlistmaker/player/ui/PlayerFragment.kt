package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import com.google.android.material.bottomsheet.BottomSheetBehavior

import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayBinding
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.GlideUrlLoader
import com.practicum.playlistmaker.player.domain.PlayerState
import com.practicum.playlistmaker.utils.AppPreferencesKeys.AN_INSTANCE_OF_THE_TRACK_CLASS
import com.practicum.playlistmaker.utils.DebounceExtension
import com.practicum.playlistmaker.utils.setDebouncedClickListener
import com.practicum.playlistmaker.utils.stopLoadingIndicator
import com.practicum.playlistmaker.medialibrary.domain.model.Playlist
import com.practicum.playlistmaker.medialibrary.ui.allplaylists.AllPlaylistsState
import com.practicum.playlistmaker.utils.AppPreferencesKeys.ONE_SECOND
import com.practicum.playlistmaker.utils.showSnackbar

class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayBinding? = null
    private val binding get() = _binding!!
    private lateinit var track: Track
    private val viewModel: PlayerViewModel by viewModel { parametersOf(track) }
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var adapter: PlayerPlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val trackFromArguments = arguments?.getSerializable(AN_INSTANCE_OF_THE_TRACK_CLASS) as? Track

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
            showSnackbar(resources.getString(R.string.error0))
        }
        setupBottomSheet()
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
        GlideUrlLoader(R.drawable.ic_placeholder).loadImage(
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
            val indicatorDelay = DebounceExtension(ONE_SECOND) {
                stopLoadingIndicator()
            }
            indicatorDelay.debounce()
        }

        // плейлистовое:
        val playlistClickListener = object : PlayerPlaylistAdapter.PlaylistClickListener {
            override fun onPlaylistClick(playlist: Playlist) {
                viewModel.addTrackToPlaylist(track, playlist)
            }
        }
        adapter = PlayerPlaylistAdapter(playlistClickListener)
    }

    private fun setupScreenState(screenState: PlayerScreenState) {
        Log.d("=== LOG ===", "===  class PlayActivity => setupScreenState ${screenState}")

        when (screenState) {
            PlayerScreenState.Initial -> {
                val playerState: PlayerState = viewModel.getState()
                setupPlayerState(playerState, 0)
            }

            is PlayerScreenState.Error -> {
                setupPlayerState(screenState.playerState, 0)
                showSnackbar(resources.getString(R.string.error0))
            }

            is PlayerScreenState.Ready -> {
                setupPlayerState(screenState.playerState, screenState.playbackPosition)
            }

            is PlayerScreenState.Content -> {
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
                showSnackbar(resources.getString(R.string.error0))
                binding.btnPlay.setImageResource(R.drawable.ic_error_notfound)
            }
        }
    }

    private fun setupAddToPlaylistButton() {
        binding.btnAddToPlaylist.setDebouncedClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
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

    private fun setupBottomSheet() {
        val bottomSheetContainer = binding.bottomSheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

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

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        binding.PlaylistsRecycler.adapter = adapter
        binding.buttonNewPlayList.setDebouncedClickListener {
            findNavController().navigate(R.id.action_playerFragment_to_newPlaylistFragment)
        }

        viewModel.statePlaylist.observe(viewLifecycleOwner) {
            when (it) {
                is AllPlaylistsState.Content -> {
                    showContent(it.playList)
                }
                else -> {}
            }
        }

        viewModel.stateAddTrack.observe(viewLifecycleOwner) {
            when (it) {
                false -> defeat()
                true -> success()
                else -> {}
            }
        }
    }

    private fun showContent(playList: List<Playlist>) {
        adapter.playlist = playList as ArrayList<Playlist>
        adapter.notifyDataSetChanged()
    }

    private fun success() {
        showSnackbar(resources.getString(R.string.addToPlaylistSuccess))
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        adapter.notifyDataSetChanged()
        viewModel.deleteValueStateAddTrack()
    }

    private fun defeat() {
        showSnackbar(resources.getString(R.string.addToPlaylistDefeat))
        viewModel.deleteValueStateAddTrack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun createArgs(track: Track): Bundle =
            bundleOf(
                AN_INSTANCE_OF_THE_TRACK_CLASS to track
            )
    }
}
