package com.practicum.playlistmaker.main.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.main.domain.MainInteractor
import com.practicum.playlistmaker.settings.domain.CommunicationButtonsInteractor
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.ui.SettingsViewModel


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

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application
                MainViewModel(
                    mainInteractor = Creator.provideGetMainInteractor(application)
                )
            }
        }
    }
}