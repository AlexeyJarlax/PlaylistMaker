package com.practicum.playlistmaker.main.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import com.practicum.playlistmaker.utils.setDebouncedClickListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding.buttonSearch.setDebouncedClickListener {
            viewModel.onSearchButtonClicked(this)
        }

        binding.buttonMediaLib.setDebouncedClickListener {
            viewModel.onMediaLibButtonClicked(this)
        }

        binding.buttonSettings.setDebouncedClickListener {
            viewModel.onSettingsButtonClicked(this)
        }
    }
}