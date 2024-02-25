package com.practicum.playlistmaker.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.presentation.TrackViewModel

class TrackActivity: ComponentActivity() {

    private lateinit var viewModel: TrackViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, TrackViewModel.getViewModelFactory("123"))[TrackViewModel::class.java]
    }
}