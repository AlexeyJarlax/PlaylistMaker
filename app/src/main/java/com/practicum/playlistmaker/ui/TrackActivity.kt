package com.practicum.playlistmaker.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.practicum.playlistmaker.presentation.TrackViewModel
import androidx.core.view.isVisible
import com.practicum.playlistmaker.databinding.ActivityTrackBinding

class TrackActivity : ComponentActivity() {
    private val viewModel by viewModels<TrackViewModel> { TrackViewModel.getViewModelFactory("123") }
    // 1
    private lateinit var binding: ActivityTrackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 2
        binding = ActivityTrackBinding.inflate(layoutInflater)
        // 3
        setContentView(binding.root)

        viewModel.getLoadingLiveData().observe(this) { isLoading ->
            changeProgressBarVisibility(isLoading)
        }
    }

    private fun changeProgressBarVisibility(visible: Boolean) {
        // 4
        binding.progressBar.isVisible = visible
    }
}