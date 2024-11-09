package com.practicum.playlistmaker.settings.domain

interface SettingsRepository {
    fun loadNightMode(): Boolean
    fun saveNightMode(value: Boolean)
    fun buttonToShareApp()
    fun buttonToHelp()
    fun buttonToSeeUserAgreement()
    fun applyTheme()
    fun sharePlaylist(message: String)
}