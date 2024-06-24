package com.practicum.playlistmaker.medialibrary.ui.ml

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

import com.practicum.playlistmaker.medialibrary.ui.favorites.FavoritesFragment
import com.practicum.playlistmaker.medialibrary.ui.allplaylists.AllPlayListsFragment

class MLViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> FavoritesFragment.newInstance()
            else -> AllPlayListsFragment.newInstance()
        }
    }
}