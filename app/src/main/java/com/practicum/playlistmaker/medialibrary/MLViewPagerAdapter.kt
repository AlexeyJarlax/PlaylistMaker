package com.practicum.playlistmaker.medialibrary

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class MLViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val plug: () -> Unit
) : FragmentStateAdapter(fragmentManager, lifecycle) {

}