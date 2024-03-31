package com.practicum.playlistmaker.medialibrary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavoritesBinding
import com.practicum.playlistmaker.utils.ArtworkUrlLoader
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MLFavoritesFragment : Fragment() {

    private lateinit var plugImageView: ImageView

    companion object {
        private const val plug = "plug"

        fun newInstance(plug: String) = MLFavoritesFragment().apply {
            arguments = Bundle().apply {
                putString(plug, plug)
            }
        }
    }

    private val thisViewModel: MLFavoritesViewModel by viewModel {
        parametersOf(requireArguments().getString(plug))
    }

    private lateinit var binding: FragmentFavoritesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        plugImageView = view.findViewById(R.id.artwork_image_view)

        thisViewModel.observeUrl().observe(viewLifecycleOwner) { plug ->
            plugImageView.setImageResource(R.drawable.ic_error_notfound)
        }
    }
}