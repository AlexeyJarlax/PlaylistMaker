package com.practicum.playlistmaker.settings.ui
import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.CommunicationButtonsInteractor


class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val communicationButtonsInteractor: CommunicationButtonsInteractor
) : ViewModel() {

    private val _isNightMode = MutableLiveData(false)
    val isNightMode: LiveData<Boolean> = _isNightMode

    init {
        _isNightMode.value = settingsInteractor.loadNightMode()
    }

    fun changeNightMode(value: Boolean) {
        if (_isNightMode.value != value) {
            _isNightMode.value = value
            settingsInteractor.saveNightMode(value)
            if (value) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun shareApp() {
        communicationButtonsInteractor.buttonToShareApp()
    }

    fun goToHelp() {
        communicationButtonsInteractor.buttonToHelp()
    }

    fun seeUserAgreement() {
        communicationButtonsInteractor.buttonToSeeUserAgreement()
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as Application
                SettingsViewModel(
                    settingsInteractor = Creator.provideSettingsInteractor(application),
                    communicationButtonsInteractor = Creator.provideCommunicationButtonsInteractor(application)
                )
            }
        }
    }

}