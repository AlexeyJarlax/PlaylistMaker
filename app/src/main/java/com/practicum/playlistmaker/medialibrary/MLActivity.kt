package com.practicum.playlistmaker.medialibrary

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMedialabBinding


class MLActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedialabBinding

    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedialabBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val plug = intent.getStringExtra("plug") ?: ""

        binding.viewPager.adapter = MLViewPagerAdapter(supportFragmentManager,
            lifecycle, plug)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }

}




