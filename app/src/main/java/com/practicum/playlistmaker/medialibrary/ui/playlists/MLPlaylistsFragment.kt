package com.practicum.playlistmaker.medialibrary.ui.playlists

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.databinding.UtilErrorLayoutForFragmentsBinding
import com.practicum.playlistmaker.utils.AppPreferencesKeys.INTERNET_EMPTY
import com.practicum.playlistmaker.utils.AppPreferencesKeys.LOADING
import com.practicum.playlistmaker.utils.AppPreferencesKeys.PLAYLISTS_EMPTY
import com.practicum.playlistmaker.utils.ErrorUtils.ifMedialibraryErrorShowPlug
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.medialibrary.domain.db.Playlist

class MLPlaylistsFragment : Fragment() {

    private val viewModel: MLPlaylistsViewModel by viewModel()
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val playlistsList = ArrayList<Playlist>()
    private lateinit var playlistsAdapter: PlaylistsBigAdapter

    companion object {
        fun newInstance() = MLPlaylistsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        val rootView = binding.root
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        viewModel.loadFromHistory()

        binding.buttonNewPlayList.setOnClickListener {

            findNavController().navigate(R.id.action_MLFragment_to_MLCreatePlaylistFragment)

//            parentFragmentManager.beginTransaction()
//                .replace(R.id.nav_host_fragment, MLCreatePlaylistFragment.kt.newInstance())
//                .addToBackStack(null)
//                .commit()


//            val fragment = MLCreatePlaylistFragment.kt().apply {
//                arguments = Bundle().apply {
//                    putSerializable(AppPreferencesKeys.AN_INSTANCE_OF_THE_TRACK_CLASS, null)
//                }
//            }
//            requireActivity().supportFragmentManager.beginTransaction()
//                .setReorderingAllowed(true)
//                .replace(R.id.nav_host_fragment, fragment)
//                .addToBackStack(null)
//                .commit()

        Log.d("=== LOG ===", "=== MLPlaylistsFragment > utilErrorLayoutBinding.retryButton.setOnClickListener > MLCreatePlaylistFragment.kt")
        }
        initializeAdapter()
    }

    private fun setupObserver() {
        viewModel.screenState.observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is MLPlaylistsScreenState.Content -> {
                    ifMedialibraryErrorShowPlug(requireContext(), PLAYLISTS_EMPTY)
//                    val favoritesList = screenState.historyList
//                    binding.testBlock2.text = favoritesList.joinToString("\n")
                }
                MLPlaylistsScreenState.Error -> {
                    ifMedialibraryErrorShowPlug(requireContext(), INTERNET_EMPTY)
                }
                MLPlaylistsScreenState.Loading -> {
                    ifMedialibraryErrorShowPlug(requireContext(), LOADING)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    private fun initializeAdapter() {
        playlistsAdapter = PlaylistsBigAdapter { playlist ->
            println("clicked ${playlist.title}")
        }
        playlistsAdapter.playlists = playlistsList
        binding.rvListPlaylists.adapter = playlistsAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showContent(screenState: MLPlaylistsScreenState.Content) {
        if (screenState.playlistsList.isNotEmpty()) {
            binding.placeholderImage.isVisible = false
            playlistsList.clear()
            playlistsList.addAll(screenState.playlistsList)
            playlistsAdapter.notifyDataSetChanged()
        } else {
            binding.placeholderImage.isVisible = true
            playlistsList.clear()
            playlistsAdapter.notifyDataSetChanged()
        }
    }

}