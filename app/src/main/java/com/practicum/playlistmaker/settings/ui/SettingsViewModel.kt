package com.practicum.playlistmaker.settings.ui

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.settings.domain.SettingsInteractor

class SettingsViewModel(private val settingsInteractor: SettingsInteractor) : ViewModel() {

    private val _isNightMode = MutableLiveData(false)
    val isNightMode: LiveData<Boolean> = _isNightMode

    init {
        _isNightMode.value = settingsInteractor.loadNightMode()
    }

    fun changeNightMode(value: Boolean) {
        if (_isNightMode.value != value) {
            _isNightMode.value = value
            settingsInteractor.saveNightMode(value)
            if (value) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

        fun shareApp() {
            settingsInteractor.buttonToShareApp()
        }

        fun goToHelp() {
            settingsInteractor.buttonToHelp()
        }

        fun seeUserAgreement() {
            settingsInteractor.buttonToSeeUserAgreement()
        }
    }
