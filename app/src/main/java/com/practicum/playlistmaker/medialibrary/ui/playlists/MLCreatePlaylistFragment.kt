package com.practicum.playlistmaker.medialibrary.ui.playlists

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentCreatePlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MLCreatePlaylistFragment : Fragment() {

    private val viewmodel: MLCreatePlaylistViewModel by viewModel()
    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private var isChangesExist: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeObservers()
        initializePickMedia()
        initializeClickListeners()
        initializeTextChangedListener()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeObservers() {
        viewmodel.screenState.observe(viewLifecycleOwner) { screenState ->
            binding.createButton.isEnabled = screenState.isTitleNotEmpty
            isChangesExist = screenState.isChangesExist
            if (screenState.uri != null) {
                binding.coverImageView.setImageURI(screenState.uri)
                binding.coverImageView.scaleType = ImageView.ScaleType.CENTER_CROP
            }
        }
    }

    private fun initializePickMedia() {
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewmodel.changeUri(uri = uri)
            } else {
                showToast(R.string.image_empty, binding.editTextTitle.text)
            }
        }
    }

    private fun initializeClickListeners() {
        binding.coverImageView.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }
        binding.createButton.setOnClickListener {
            viewmodel.createClick()
            showToast(R.string.playlist_created, binding.editTextTitle.text)
            findNavController().navigateUp()
        }
        binding.backButton.setOnClickListener { showExitDialog() }
        activity?.onBackPressedDispatcher?.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitDialog()
            }
        })
    }
    private fun initializeTextChangedListener() {
        binding.editTextTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewmodel.changeTitle(s.toString())
            }
        })
        binding.editTextDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewmodel.changeDescription(s.toString())
            }
        })
    }

    private fun showExitDialog() {
        if (isChangesExist) {
            MaterialAlertDialogBuilder(requireActivity())
                .setTitle(getString(R.string.dialog_title))
                .setMessage(getString(R.string.dialog_text))
                .setNeutralButton(getString(R.string.dialog_cancel)) { _, _ ->
                    //close dialog
                }.setPositiveButton(R.string.dialog_ok) { _, _ ->
                    findNavController().navigateUp()
                }.show()
        } else {
            findNavController().navigateUp()
        }
    }

    private fun showToast(text: Int, text1: Editable) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }

}