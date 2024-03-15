package com.practicum.playlistmaker.main.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import com.practicum.playlistmaker.utils.setDebouncedClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSearch.setDebouncedClickListener {
            viewModel.onSearchButtonClicked()
        }

        binding.buttonMediaLib.setDebouncedClickListener {
            viewModel.onMediaLibButtonClicked()
        }

        binding.buttonSettings.setDebouncedClickListener {
            viewModel.onSettingsButtonClicked()
        }
    }
}