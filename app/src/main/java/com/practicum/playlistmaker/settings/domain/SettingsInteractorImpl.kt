package com.practicum.playlistmaker.settings.domain

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository) :

    SettingsInteractor {

    override fun loadNightMode(): Boolean {
        return settingsRepository.loadNightMode()
    }

    override fun saveNightMode(value: Boolean) {
        settingsRepository.saveNightMode(value)
    }

    override fun buttonToShareApp() {
        settingsRepository.buttonToShareApp()
    }

    override fun buttonToHelp() {
        settingsRepository.buttonToHelp()
    }

    override fun buttonToSeeUserAgreement() {
        settingsRepository.buttonToSeeUserAgreement()
    }

    override fun applyTheme() {
        settingsRepository.applyTheme()
    }

    override fun sharePlaylist(message: String) {
        settingsRepository.sharePlaylist(message)
    }
}