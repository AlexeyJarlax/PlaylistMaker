package com.practicum.playlistmaker.medialibrary.playlists

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentCreatePlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class MLCreatePlaylistFragment : Fragment() {

    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MLCreatePlaylistViewModel by viewModel()
    private var coverUri: Uri? = null

    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            coverUri = it
            binding.coverImageView.setImageURI(it)
        }
    }

    companion object {
        fun newInstance() = MLCreatePlaylistFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.coverImageView.setOnClickListener {
            selectImageLauncher.launch("image/*")
        }

        binding.editTextTitle.doOnTextChanged { text, _, _, _ ->
            binding.createButton.isEnabled = !text.isNullOrEmpty()
        }

        binding.createButton.setOnClickListener {
            createPlaylist()
        }

        binding.backButton.setOnClickListener {
            showExitConfirmationDialog()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            showExitConfirmationDialog()
        }
    }

    private fun createPlaylist() {
        val title = binding.editTextTitle.text.toString()
        val description = binding.editTextDescription.text.toString()
        var coverPath: String? = null

        coverUri?.let {
            val bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, it)
            } else {
                val source = ImageDecoder.createSource(requireActivity().contentResolver, it)
                ImageDecoder.decodeBitmap(source)
            }

            coverPath = saveCoverImage(bitmap, title)
        }

        viewModel.createPlaylist(title, description, coverPath)

        Toast.makeText(requireContext(), "Плейлист \"$title\" создан", Toast.LENGTH_SHORT).show()
        parentFragmentManager.popBackStack()
    }

    private fun saveCoverImage(bitmap: Bitmap, title: String): String {
        val coverFile = File(requireContext().filesDir, "$title-cover.png")
        val outputStream = FileOutputStream(coverFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.close()
        return coverFile.absolutePath
    }

    private fun showExitConfirmationDialog() {
        if (binding.editTextTitle.text.isNullOrEmpty() && binding.editTextDescription.text.isNullOrEmpty() && coverUri == null) {
            parentFragmentManager.popBackStack()
            return
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setPositiveButton("Завершить") { _, _ -> parentFragmentManager.popBackStack() }
            .setNegativeButton("Отмена", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}