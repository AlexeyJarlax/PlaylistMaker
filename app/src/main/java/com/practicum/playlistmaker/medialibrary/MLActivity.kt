package com.practicum.playlistmaker.medialibrary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMedialabBinding
import com.practicum.playlistmaker.utils.bindGoBackButton

class MLActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedialabBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedialabBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = MLViewPagerAdapter(supportFragmentManager,
            lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()
        bindGoBackButton()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }

}




