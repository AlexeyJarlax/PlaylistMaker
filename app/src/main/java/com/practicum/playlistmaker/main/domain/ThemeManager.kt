package com.practicum.playlistmaker.main.domain

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.utils.AppPreferencesKeys
import com.practicum.playlistmaker.utils.SharedPreferencesMethods

object ThemeManager { // проверка и применение MODE_NIGHT на старте приложения

    fun applyTheme(context: Context) {
        val nightModeEnabled = SharedPreferencesMethods(context).getBooleanFromSP(AppPreferencesKeys.KEY_NIGHT_MODE, false)
        if (nightModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}