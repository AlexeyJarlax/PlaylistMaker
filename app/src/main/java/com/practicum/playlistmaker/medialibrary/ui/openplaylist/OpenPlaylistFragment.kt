package com.practicum.playlistmaker.medialibrary.ui.openplaylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentOpenPlaylistBinding
import com.practicum.playlistmaker.medialibrary.ui.editplaylist.EditPlaylistFragment
import com.practicum.playlistmaker.player.ui.PlayerFragment
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.AppPreferencesKeys
import com.practicum.playlistmaker.utils.AppPreferencesKeys.PLAYLIST_KEY
import com.practicum.playlistmaker.utils.ArtworkUrlLoader
import com.practicum.playlistmaker.utils.DebounceExtension

class OpenPlaylistFragment : Fragment() {

    private var playlistId: Int? = null

    private val viewModel: OpenPlaylistViewModel by viewModel {
        parametersOf(playlistId)
    }

    private lateinit var binding: FragmentOpenPlaylistBinding
    private var isClickAllowed = true
    private lateinit var onTrackClickDebounce: (Track) -> Unit
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bottomSheetBehaviorMenu: BottomSheetBehavior<ConstraintLayout>
    private val artworkUrlLoader = ArtworkUrlLoader()

    private val trackClickListener = object : OpenPlaylistTrackAdapter.TrackClickListener {
        override fun onTrackClick(track: Track) {
            if (isClickAllowed) {
                isClickAllowed = false

                findNavController().navigate(
                    R.id.action_openPlaylistFragment_to_playerFragment,
                    PlayerFragment.createArgs(track)
                )
                onTrackClickDebounce(track)
            }
        }

        override fun onTrackLongClick(track: Track): Boolean {
            showDialogForDeleteTrack(track)
            return true
        }
    }

    private val trackAdapter = OpenPlaylistTrackAdapter(trackClickListener)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOpenPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistId = requireArguments().getInt(PLAYLIST_KEY)
        binding.rvListPlaylists.adapter = trackAdapter

        viewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is OpenPlaylistState.Content -> showPlaylist(it)
                is OpenPlaylistState.Delete -> findNavController().navigateUp()
            }
        }

        val debounceExtension = DebounceExtension(AppPreferencesKeys.ONE_SECOND) {
            isClickAllowed = true
        }

        onTrackClickDebounce = { track ->
            debounceExtension.debounce()
            requireActivity().lifecycleScope
        }

        binding.btnGoBack.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val bottomSheetContainerTrack = binding.bottomSheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainerTrack)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        val bottomSheetContainerMenu = binding.bottomSheetMenu
        bottomSheetBehaviorMenu = BottomSheetBehavior.from(bottomSheetContainerMenu)
        bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehaviorMenu.addBottomSheetCallback(object :
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

        binding.share.setOnClickListener {
            sharePlaylist()
        }

        binding.shareText.setOnClickListener {
            sharePlaylist()
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.menu.setOnClickListener {
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.deletePlaylist.setOnClickListener {
            showDialogForDeletePlaylist()
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.editInfo.setOnClickListener {
            findNavController().navigate(
                R.id.action_openPlaylistFragment_to_editPlaylistFragment,
                EditPlaylistFragment.createArgs(playlistId!!)
            )
        }
    }


    private fun showPlaylist(playlist: OpenPlaylistState.Content) {
        binding.playlistName.text = playlist.playlistName
        if (playlist.playlistDetails?.isNotEmpty() == true) {
            binding.playlistDetails.visibility = View.VISIBLE
            binding.playlistDetails.text = playlist.playlistDetails
        } else {
            binding.playlistDetails.visibility = View.GONE
        }
        artworkUrlLoader.loadImage(playlist.imageUrl, binding.coverPlaylist)

        if (!playlist.listTracks.isNullOrEmpty()) {
            trackAdapter.tracks = playlist.listTracks as ArrayList<Track>
            trackAdapter.notifyDataSetChanged()
        }
        binding.playlistDuration.text = playlist.playlistDuration
        binding.playlistTracksCount.text = playlist.playlistCountTrack
        if (trackAdapter.tracks.isEmpty()) {
            binding.placeholderMessage.visibility = View.VISIBLE
        } else {
            binding.placeholderMessage.visibility = View.GONE
        }
        artworkUrlLoader.loadImage(playlist.imageUrl, binding.playlistCover)
        binding.playlistCover.clipToOutline = true
        binding.playlistName.text = playlist.playlistName
        binding.trackCount.text = playlist.playlistCountTrack
    }

    private fun showDialogForDeleteTrack(track: Track) {
        MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialog)
            .setTitle(R.string.delete_track)
            .setMessage(R.string.delete_track2)
            .setNeutralButton(R.string.no) { _, _ ->
            }
            .setNegativeButton(R.string.yes) { _, _ ->
                track.trackId?.let {
                    viewModel.deleteTrackFromPlaylist(it)
                }
            }.show()
    }

    private fun showDialogForDeletePlaylist() {
        MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialog)
            .setTitle(R.string.deletePlaylist)
            .setMessage(R.string.deletePlaylist2)
            .setNeutralButton(R.string.no) { _, _ ->
            }
            .setNegativeButton(R.string.yes) { _, _ ->
                viewModel.deletePlaylist()
            }.show()
    }

    override fun onStart() {
        super.onStart()
        viewModel.updatePlaylist()
    }

    private fun sharePlaylist() {
        if (trackAdapter.tracks.isEmpty()) {
            Toast.makeText(
                requireActivity(),
                requireActivity().getString(R.string.havent_created_any_track),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            viewModel.sharePlaylist()
        }
    }

    companion object {
        fun createArgs(idPlaylist: Int): Bundle {
            return bundleOf(PLAYLIST_KEY to idPlaylist)
        }
    }
}