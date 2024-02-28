package com.practicum.playlistmaker.settings.domain

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository) :
    SettingsInteractor {

    override fun loadNightMode(): Boolean {
        return settingsRepository.loadNightMode()
    }

    override fun saveNightMode(value: Boolean) {
        settingsRepository.saveNightMode(value)
    }
}