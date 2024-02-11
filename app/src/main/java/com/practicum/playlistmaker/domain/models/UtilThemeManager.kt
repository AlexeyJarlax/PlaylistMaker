package com.practicum.playlistmaker.domain.models

// темы и стили

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

object UtilThemeManager { //РЕАЛИЗАЦИЯ КНОПКИ НОЧНОЙ И ДНЕВНОЙ ТЕМЫ

    fun applyTheme(context: Context) {
        val sharedPreferences = context.getSharedPreferences(AppPreferencesKeys.PREFS_NAME, Context.MODE_PRIVATE)
        val nightModeEnabled = sharedPreferences.getBoolean(AppPreferencesKeys.KEY_NIGHT_MODE, false)
        if (nightModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun isNightModeEnabled(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(AppPreferencesKeys.PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(AppPreferencesKeys.KEY_NIGHT_MODE, false)
    }

    fun setNightModeEnabled(context: Context, enabled: Boolean) {
        val sharedPreferences = context.getSharedPreferences(AppPreferencesKeys.PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(AppPreferencesKeys.KEY_NIGHT_MODE, enabled)
        editor.apply()
        applyTheme(context)
    }
}