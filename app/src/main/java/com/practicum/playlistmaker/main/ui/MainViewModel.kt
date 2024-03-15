package com.practicum.playlistmaker.main.ui

import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.main.domain.MainInteractor


class MainViewModel(private val mainInteractor: MainInteractor) : ViewModel() {

    fun onSearchButtonClicked() {
        mainInteractor.navigateToSearch()
    }

    fun onMediaLibButtonClicked() {
        mainInteractor.navigateToMediaLib()
    }

    fun onSettingsButtonClicked() {
        mainInteractor.navigateToSettings()
    }
}