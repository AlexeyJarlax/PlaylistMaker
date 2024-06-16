package com.practicum.playlistmaker.medialibrary.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel

import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavoritesBinding
import com.practicum.playlistmaker.player.ui.PlayerFragment
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.AppPreferencesKeys.FAVORITES_EMPTY
import com.practicum.playlistmaker.utils.AppPreferencesKeys.ONE_SECOND
import com.practicum.playlistmaker.utils.DebounceExtension
import com.practicum.playlistmaker.utils.ErrorUtils.ifMedialibraryErrorShowPlug

class FavoritesFragment : Fragment() {

    private val aboutViewModel: FavoritesViewModel by viewModel()
    private lateinit var binding: FragmentFavoritesBinding
    private var isClickAllowed = true
    private lateinit var favoritesRecyclerView: RecyclerView
    private lateinit var onTrackClickDebounce: (Track) -> Unit

    private val trackClickListener = object : TrackAdapter.TrackClickListener {
        override fun onTrackClick(track: Track) {
            if (isClickAllowed) {
                isClickAllowed = false
                findNavController().navigate(
                    R.id.action_libraryFragment_to_trackFragment,
                    PlayerFragment.createArgs(track)
                )
                aboutViewModel.addTrackToHistory(track)
                onTrackClickDebounce(track)
            }
        }
    }
    private val trackAdapter = TrackAdapter(trackClickListener)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoritesRecyclerView = binding.favoritesRecyclerView
        binding.favoritesRecyclerView.adapter = trackAdapter

        val debounceExtension = DebounceExtension(ONE_SECOND) {
            isClickAllowed = true
        }

        onTrackClickDebounce = { track ->
            debounceExtension.debounce()
            requireActivity().lifecycleScope
        }
        setupObserver()
    }

    private fun setupObserver() {
        aboutViewModel.stateLiveData.observe(viewLifecycleOwner) { stateLiveData ->
            when (stateLiveData) {
                is FavoriteState.Ready-> showContent(stateLiveData)
                FavoriteState.Error -> {
                    showEmpty()
                }
                FavoriteState.Loading -> {}
                else -> {}
            }
        }
    }

    private fun showEmpty() {
        ifMedialibraryErrorShowPlug(requireContext(), FAVORITES_EMPTY)
        trackAdapter.tracks.clear()
        trackAdapter.notifyDataSetChanged()
    }

    private fun showContent(stateLiveData: FavoriteState.Ready) {
        trackAdapter.tracks.clear()
        trackAdapter.tracks.addAll(stateLiveData.favoritesList)
        trackAdapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance(): Fragment {
            return FavoritesFragment()
        }
    }
}