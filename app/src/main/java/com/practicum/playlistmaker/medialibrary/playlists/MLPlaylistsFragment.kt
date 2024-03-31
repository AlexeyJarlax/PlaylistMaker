package com.practicum.playlistmaker.medialibrary.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.utils.AppPreferencesKeys.INTERNET_EMPTY
import com.practicum.playlistmaker.utils.AppPreferencesKeys.LOADING
import com.practicum.playlistmaker.utils.AppPreferencesKeys.PLAYLISTS_EMPTY
import com.practicum.playlistmaker.utils.ErrorUtils.ifFragmentErrorShowPlug
import org.koin.androidx.viewmodel.ext.android.viewModel

class MLPlaylistsFragment : Fragment() {

    private val viewModel: MLPlaylistsViewModel by viewModel()
    private lateinit var binding: FragmentPlaylistsBinding

    companion object {
        fun newInstance() = MLPlaylistsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        viewModel.loadFromHistory()
    }

    private fun setupObserver() {
        viewModel.screenState.observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is MLPlaylistsScreenState.Ready -> {
                    ifFragmentErrorShowPlug(requireContext(), PLAYLISTS_EMPTY)
//                    val favoritesList = screenState.historyList
//                    binding.testBlock2.text = favoritesList.joinToString("\n")
                }
                MLPlaylistsScreenState.Error -> {
                    ifFragmentErrorShowPlug(requireContext(), INTERNET_EMPTY)
                }
                MLPlaylistsScreenState.Loading -> {
                    ifFragmentErrorShowPlug(requireContext(), LOADING)
                }
            }
        }
    }
}