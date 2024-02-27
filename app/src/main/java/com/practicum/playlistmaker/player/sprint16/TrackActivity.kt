package com.practicum.playlistmaker.player.sprint16

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.practicum.playlistmaker.player.sprint16.TrackViewModel
import androidx.core.view.isVisible
import com.practicum.playlistmaker.databinding.ActivityTrackBinding

class TrackActivity : ComponentActivity() {
    private val viewModel by viewModels<TrackViewModel> { TrackViewModel.getViewModelFactory("123") }
    private lateinit var binding: ActivityTrackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTrackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getScreenStateLiveData().observe(this) { screenState ->
            when (screenState) {
                is TrackScreenState.Content -> {
                    changeContentVisibility(loading = false)
                    binding.picture.setImage(screenState.trackModel.pictureUrl)
                    binding.author.text = screenState.trackModel.author
                    binding.trackName.text = screenState.trackModel.name
                }
                is TrackScreenState.Loading -> {
                    changeContentVisibility(loading = true)
                }
            }
        }
        // 1
        viewModel.getPlayStatusLiveData().observe(this) { playStatus ->
            changeButtonStyle(playStatus)
            // 2
            binding.seekBar.value = playStatus.progress
        }
    }

    private fun changeContentVisibility(loading: Boolean) {
        binding.progressBar.isVisible = loading

        binding.playButton.isVisible = !loading
        binding.picture.isVisible = !loading
        binding.author.isVisible = !loading
        binding.trackName.isVisible = !loading
    }

    private fun changeButtonStyle(playStatus: PlayStatus) {
        // Меняем вид кнопки проигрывания в зависимости от того, играет сейчас трек или нет
    }
}