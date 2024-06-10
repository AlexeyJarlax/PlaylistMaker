package com.practicum.playlistmaker.medialibrary.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel

import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.medialibrary.domain.model.Playlist

class PlaylistFragment : Fragment() {
    private val playlistViewModel: PlaylistViewModel by viewModel()
    private lateinit var binding: FragmentPlaylistBinding
    private val playlistAdapter = PlaylistAdapter()
    private val utilErrorBox = view?.findViewById<LinearLayout>(R.id.utilErrorBoxForFragments)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistViewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is PlaylistState.Content -> {
                    showContent(it.playList)
                }

                is PlaylistState.Empty -> showError()
            }
        }

        binding.PlaylistsRecycler.adapter = playlistAdapter

        binding.buttonNewPlayList.setOnClickListener {
            findNavController().navigate(R.id.action_libraryFragment_to_newPlaylistFragment2)
        }
    }

    private fun showError() {
        utilErrorBox?.visibility = View.VISIBLE
    }

    private fun showContent(playList: List<Playlist>) {
        utilErrorBox?.visibility = View.GONE
        playlistAdapter.playlist = playList as ArrayList<Playlist>
        playlistAdapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance(): Fragment {
            return PlaylistFragment()
        }
    }
}