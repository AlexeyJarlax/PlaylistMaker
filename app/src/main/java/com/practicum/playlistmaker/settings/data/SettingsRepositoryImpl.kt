package com.practicum.playlistmaker.settings.data

import android.content.SharedPreferences
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import com.practicum.playlistmaker.utils.AppPreferencesKeys.KEY_NIGHT_MODE

class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    SettingsRepository {

    override fun loadNightMode(): Boolean {
        return sharedPreferences.getBoolean(KEY_NIGHT_MODE, false)
    }

    override fun saveNightMode(value: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_NIGHT_MODE, value).apply()
    }
}
