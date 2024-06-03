package com.practicum.playlistmaker.medialibrary.playlists

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.databinding.UtilErrorLayoutForFragmentsBinding
import com.practicum.playlistmaker.player.ui.PlayFragment
import com.practicum.playlistmaker.utils.AppPreferencesKeys
import com.practicum.playlistmaker.utils.AppPreferencesKeys.INTERNET_EMPTY
import com.practicum.playlistmaker.utils.AppPreferencesKeys.LOADING
import com.practicum.playlistmaker.utils.AppPreferencesKeys.PLAYLISTS_EMPTY
import com.practicum.playlistmaker.utils.ErrorUtils.ifMedialibraryErrorShowPlug
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.navigation.fragment.findNavController

class MLPlaylistsFragment : Fragment() {

    private val viewModel: MLPlaylistsViewModel by viewModel()

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private var _utilErrorLayoutBinding: UtilErrorLayoutForFragmentsBinding? = null
    private val utilErrorLayoutBinding get() = _utilErrorLayoutBinding!!

    companion object {
        fun newInstance() = MLPlaylistsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        val rootView = binding.root
        _utilErrorLayoutBinding = UtilErrorLayoutForFragmentsBinding.bind(rootView.findViewById(R.id.utilErrorBoxForFragments))
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        viewModel.loadFromHistory()

        utilErrorLayoutBinding.retryButton.setOnClickListener {

            findNavController().navigate(R.id.action_MLFragment_to_MLCreatePlaylistFragment)

//            parentFragmentManager.beginTransaction()
//                .replace(R.id.nav_host_fragment, MLCreatePlaylistFragment.newInstance())
//                .addToBackStack(null)
//                .commit()


//            val fragment = MLCreatePlaylistFragment().apply {
//                arguments = Bundle().apply {
//                    putSerializable(AppPreferencesKeys.AN_INSTANCE_OF_THE_TRACK_CLASS, null)
//                }
//            }
//            requireActivity().supportFragmentManager.beginTransaction()
//                .setReorderingAllowed(true)
//                .replace(R.id.nav_host_fragment, fragment)
//                .addToBackStack(null)
//                .commit()

        Log.d("=== LOG ===", "=== MLPlaylistsFragment > utilErrorLayoutBinding.retryButton.setOnClickListener > MLCreatePlaylistFragment")
        }
    }

    private fun setupObserver() {
        viewModel.screenState.observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is MLPlaylistsScreenState.Ready -> {
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
        _utilErrorLayoutBinding = null
    }
}