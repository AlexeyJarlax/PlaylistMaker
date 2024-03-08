package com.practicum.playlistmaker.main.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import com.practicum.playlistmaker.utils.setDebouncedClickListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModelProvider: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelProvider = ViewModelProvider(
            this,
            MainViewModel.getViewModelFactory()
        )[MainViewModel::class.java]


        binding.buttonSearch.setDebouncedClickListener {
            viewModelProvider.onSearchButtonClicked()
        }

        binding.buttonMediaLib.setDebouncedClickListener {
            viewModelProvider.onMediaLibButtonClicked()
        }

        binding.buttonSettings.setDebouncedClickListener {
            viewModelProvider.onSettingsButtonClicked()
        }
    }
}