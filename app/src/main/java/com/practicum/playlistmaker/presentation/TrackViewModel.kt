package com.practicum.playlistmaker.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.practicum.playlistmaker.Creator
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.practicum.playlistmaker.MyApplication
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

class TrackViewModel(
    private val trackId: String,
    private val tracksInteractor: TracksInteractor,
) : ViewModel() {

    init {
        Log.d("TEST", "init!: $trackId")
    }

    companion object {
        // 1
        fun getViewModelFactory(trackId: String): ViewModelProvider.Factory = viewModelFactory {
            // 2
            initializer {
                // 3
                val interactor = (this[APPLICATION_KEY] as MyApplication).provideTracksInteractor()

                TrackViewModel(
                    trackId,
                    interactor,
                )
            }
        }
    }
}