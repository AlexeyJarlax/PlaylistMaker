package com.practicum.playlistmaker.medialibrary.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentFavoritesBinding
import com.practicum.playlistmaker.utils.AppPreferencesKeys.FAVORITES_EMPTY
import com.practicum.playlistmaker.utils.AppPreferencesKeys.INTERNET_EMPTY
import com.practicum.playlistmaker.utils.AppPreferencesKeys.LOADING
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.practicum.playlistmaker.utils.ErrorUtils.ifFragmentErrorShowPlug

class MLFavoritesFragment : Fragment() {

    private val viewModel: MLFavoritesViewModel by viewModel()
    private lateinit var binding: FragmentFavoritesBinding

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
        setupObserver()
        viewModel.loadFromHistory() // значение сохраненных в память треков
    }

    private fun setupObserver() {
        viewModel.screenState.observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is MLFavoritesScreenState.Ready -> {
                    ifFragmentErrorShowPlug(requireContext(), FAVORITES_EMPTY)
//                    val favoritesList = screenState.historyList
//                    binding.testBlock1.text = favoritesList.joinToString("\n")
                }
                MLFavoritesScreenState.Error -> {
                    ifFragmentErrorShowPlug(requireContext(), INTERNET_EMPTY)
                }
                MLFavoritesScreenState.Loading -> {
                    ifFragmentErrorShowPlug(requireContext(), LOADING)
                }
            }
        }
    }
}