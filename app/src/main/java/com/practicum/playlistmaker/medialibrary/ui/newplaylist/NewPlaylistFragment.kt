package com.practicum.playlistmaker.medialibrary.ui.newplaylist

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.practicum.playlistmaker.utils.setDebouncedClickListener
import com.practicum.playlistmaker.utils.showSnackbar
import com.practicum.playlistmaker.utils.startLoadingIndicator
import com.practicum.playlistmaker.utils.stopLoadingIndicator

open class NewPlaylistFragment : Fragment() {

    open lateinit var binding: FragmentNewPlaylistBinding
    open val playlistViewModel: NewPlaylistViewModel by viewModel()
    private var playlistName: String = ""
    private var playlistDescription: String = ""
    private var playlistCoverUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        handleBackNavigation()
        restoreInstanceState(savedInstanceState)
    }

    private fun initUI() {
        binding.inputName.doOnTextChanged { text, _, _, _ ->
            playlistName = text?.toString().orEmpty()
            binding.createPlaylist.isEnabled = playlistName.isNotBlank()
        }

        binding.inputDescription.doOnTextChanged { text, _, _, _ ->
            playlistDescription = text?.toString().orEmpty()
        }

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                playlistCoverUri = uri
                binding.addPicture.apply {
                    setImageURI(uri)
                    clipToOutline = true
                }
            }
        }

        binding.addPicture.setDebouncedClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        playlistViewModel.observeState().observe(viewLifecycleOwner) { state ->
            if (state is NewPlaylistState.Success) {
                showSnackbar("Плейлист $playlistName создан")
                findNavController().navigateUp()
                stopLoadingIndicator()
            }
        }

        binding.createPlaylist.setDebouncedClickListener {
            startLoadingIndicator()
            playlistViewModel.addPlaylist(playlistName, playlistDescription, playlistCoverUri)
        }

        binding.btnBackFromNewPlayList.setNavigationOnClickListener {
            handleUnsavedData()
        }
    }

    private fun handleBackNavigation() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleUnsavedData()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun handleUnsavedData() {
        if (playlistCoverUri != null || playlistName.isNotEmpty() || playlistDescription.isNotEmpty()) {
            showConfirmDialog()
        } else {
            findNavController().navigateUp()
        }
    }

    private fun showConfirmDialog() {
        MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialog)
            .setTitle(resources.getString(R.string.finishCreatingPlaylist))
            .setMessage(resources.getString(R.string.unsavedDataWillBeLost))
            .setNeutralButton(resources.getString(R.string.cancel), null)
            .setNegativeButton(resources.getString(R.string.end)) { _, _ ->
                findNavController().navigateUp()
            }
            .show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        playlistCoverUri?.let {
            outState.putString("playlistCover", it.toString())
        }
    }

    private fun restoreInstanceState(savedInstanceState: Bundle?) {
        savedInstanceState?.getString("playlistCover")?.toUri()?.let {
            playlistCoverUri = it
            binding.addPicture.apply {
                setImageURI(it)
                clipToOutline = true
            }
        }
    }
}