package com.practicum.playlistmaker.settings.domain

interface SettingsRepository {
    fun loadNightMode(): Boolean
    fun saveNightMode(value: Boolean)
}