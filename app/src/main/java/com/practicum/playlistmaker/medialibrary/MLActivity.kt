package com.practicum.playlistmaker.medialibrary

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MLActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val plug = intent.getStringExtra("заглушечка") ?: "незуглушечка"

        binding.viewPager.adapter = MLViewPagerAdapter(supportFragmentManager,
            lifecycle, plug)

    }

    private val adapter = MLViewPagerAdapter {
        if (clickDebounce()) {
            val intent = Intent(this, MLActivity::class.java)
            intent.putExtra("plug", it.image)
            startActivity(intent)
        }
    }
}





