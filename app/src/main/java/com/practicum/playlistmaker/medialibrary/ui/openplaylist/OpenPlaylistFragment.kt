package com.practicum.playlistmaker.medialibrary.ui.openplaylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.practicum.playlistmaker.utils.GlideUrlLoader
import com.practicum.playlistmaker.utils.DebounceExtension
import com.practicum.playlistmaker.utils.showSnackbar

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
        GlideUrlLoader(R.drawable.ic_playlist_placeholder).loadImage(playlist.imageUrl, binding.playlistCover)

        if (!playlist.listTracks.isNullOrEmpty()) {
            trackAdapter.tracks = playlist.listTracks as ArrayList<Track>
            trackAdapter.notifyDataSetChanged()
        }
        binding.playlistDuration.text = playlist.playlistDuration
        binding.tracksCount.text = playlist.playlistCountTrack
        if (trackAdapter.tracks.isEmpty()) {
            binding.placeholderMessage.visibility = View.VISIBLE
        } else {
            binding.placeholderMessage.visibility = View.GONE
        }
        GlideUrlLoader(R.drawable.ic_placeholder).loadImage(playlist.imageUrl, binding.playlistCover2)
        binding.playlistCover2.clipToOutline = true
        binding.playlistName2.text = playlist.playlistName
        binding.trackCount2.text = playlist.playlistCountTrack
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
            showSnackbar(resources.getString(R.string.havent_created_any_track))
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