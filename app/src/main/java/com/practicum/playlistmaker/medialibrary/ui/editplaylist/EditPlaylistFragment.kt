package com.practicum.playlistmaker.medialibrary.ui.editplaylist

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.medialibrary.ui.newplaylist.NewPlaylistFragment
import com.practicum.playlistmaker.utils.AppPreferencesKeys.PLAYLIST_KEY
import com.practicum.playlistmaker.utils.GlideUrlLoader
import com.practicum.playlistmaker.utils.setDebouncedClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class EditPlaylistFragment : NewPlaylistFragment() {

    override val playlistViewModel: EditPlaylistViewModel by viewModel() {
        parametersOf(requireArguments().getInt(PLAYLIST_KEY))
    }
    private var playlistName: String = ""
    private var playlistDescription: String = ""
    private var playlistCoverUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnBackFromNewPlayList.title = requireActivity().getString(R.string.edit_info)
        binding.createPlaylist.text = requireActivity().getString(R.string.save)
        playlistViewModel.observeStateEditPlaylist().observe(viewLifecycleOwner) {
            when (it) {
                is EditPlaylistState.Content -> showData(it)
            }
        }

        binding.inputName.doOnTextChanged { text, start, before, count ->
            if (text != null) {
                playlistName = text.toString()
                binding.createPlaylist.isEnabled = text.isNotBlank()
            }
        }

        binding.inputDescription.doOnTextChanged { text, start, before, count ->
            playlistDescription = text.toString()
        }

        binding.createPlaylist.setDebouncedClickListener {
            playlistViewModel.editPlaylist(playlistCoverUri, playlistName, playlistDescription)
            findNavController().navigateUp()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(enabled = true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            })

        binding.btnBackFromNewPlayList.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    playlistCoverUri = uri
                    binding.addPicture.setImageURI(uri)
                    binding.addPicture.clipToOutline = true
                }
            }

        binding.addPicture.setDebouncedClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun showData(editPlaylistState: EditPlaylistState.Content) {
        binding.inputDescription.setText(editPlaylistState.playlistDescription)
        binding.inputName.setText(editPlaylistState.playlistName)

        if (editPlaylistState.imageUrl != null){
            GlideUrlLoader(R.drawable.pl_plus_photo).loadImage(editPlaylistState.imageUrl, binding.addPicture)
            binding.addPicture.clipToOutline = true
        }
    }

    companion object {
        fun createArgs(idPlaylist: Int): Bundle {
            return bundleOf(PLAYLIST_KEY to idPlaylist)
        }
    }
}