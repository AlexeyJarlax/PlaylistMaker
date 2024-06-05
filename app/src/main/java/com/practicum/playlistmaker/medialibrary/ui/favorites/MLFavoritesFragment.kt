package com.practicum.playlistmaker.medialibrary.ui.favorites

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavoritesBinding
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.AppPreferencesKeys
import com.practicum.playlistmaker.utils.AppPreferencesKeys.FAVORITES_EMPTY
import com.practicum.playlistmaker.utils.AppPreferencesKeys.INTERNET_EMPTY
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.practicum.playlistmaker.utils.ErrorUtils.ifMedialibraryErrorShowPlug
import androidx.navigation.fragment.findNavController

class MLFavoritesFragment : Fragment() {

    private val viewModel: MLFavoritesViewModel by viewModel()
    private lateinit var binding: FragmentFavoritesBinding
    private val favoriteTrackList = ArrayList<Track>()
    private lateinit var favoritesRecyclerView: RecyclerView
    private lateinit var favoriteTracksAdapter: AdapterForFavorites


    companion object {
        fun newInstance() = MLFavoritesFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoritesRecyclerView = binding.favoritesRecyclerView
        setupAdapterForFavorites()
        setupLayoutManager()
        setupObserver()
    }

    private fun setupAdapterForFavorites() {
        favoriteTracksAdapter = AdapterForFavorites { track ->
//            val fragment = PlayFragment().apply {
//                arguments = Bundle().apply {
//                    putSerializable(AppPreferencesKeys.AN_INSTANCE_OF_THE_TRACK_CLASS, track)
//                }
//            }
            findNavController().navigate(
                R.id.action_MLFragment_to_playFragment,
                Bundle().apply {
                    putSerializable(AppPreferencesKeys.AN_INSTANCE_OF_THE_TRACK_CLASS, track)
                }
            )
//            requireActivity().supportFragmentManager.beginTransaction()
//                .setReorderingAllowed(true)
//                .replace(R.id.nav_host_fragment, fragment)
//                .addToBackStack(null)
//                .commit()
        }
        Log.d("=== LOG ===", "=== MLFavoritesFragment > setupAdapterForFavorites()")
        favoriteTracksAdapter.favoritTracks = favoriteTrackList
        binding.favoritesRecyclerView.adapter = favoriteTracksAdapter
    }

    private fun setupLayoutManager() {
        if (favoritesRecyclerView.layoutManager != null) {
        } else {
            val layoutManager by lazy { LinearLayoutManager(requireContext()) }
            favoritesRecyclerView.layoutManager = layoutManager
        }
    }

    private fun setupObserver() {
        viewModel.screenState.observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is MLFavoritesScreenState.Ready -> showContent(screenState)
                MLFavoritesScreenState.Error -> {
                    ifMedialibraryErrorShowPlug(requireContext(), INTERNET_EMPTY)
                }
                MLFavoritesScreenState.Loading -> {}
            }
        }
    }

    @SuppressLint
    private fun showContent(screenState: MLFavoritesScreenState.Ready) {
        Log.d("=== LOG ===", "=== MLFavoritesFragment > showContent")
        if (screenState.favoritesList.isNotEmpty()) {
            favoriteTrackList.clear()
            favoriteTrackList.addAll(screenState.favoritesList)
            favoriteTracksAdapter.notifyDataSetChanged()
            binding.favoritesRecyclerView.isVisible = true
        } else {
            favoriteTrackList.clear()
            favoriteTracksAdapter.notifyDataSetChanged()
            ifMedialibraryErrorShowPlug(requireContext(), FAVORITES_EMPTY)
        }
    }
}