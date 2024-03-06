package com.practicum.playlistmaker.main.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
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

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application
                MainViewModel(
                    mainInteractor = Creator.provideMainInteractor(application)
                )
            }
        }
    }
}