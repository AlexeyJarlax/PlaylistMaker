package com.practicum.playlistmaker.main.ui

import androidx.lifecycle.ViewModel
import android.content.Context
import com.practicum.playlistmaker.main.domain.MainUseCase

class MainViewModel : ViewModel() {

    private val mainUseCase = MainUseCase()

    fun onSearchButtonClicked(context: Context) {
        mainUseCase.navigateToSearch(context)
    }

    fun onMediaLibButtonClicked(context: Context) {
        mainUseCase.navigateToMediaLib(context)
    }

    fun onSettingsButtonClicked(context: Context) {
        mainUseCase.navigateToSettings(context)
    }
}