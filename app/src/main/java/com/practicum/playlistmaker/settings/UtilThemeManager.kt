package com.practicum.playlistmaker.settings

// темы и стили

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.utils.AppPreferencesKeys
import com.practicum.playlistmaker.utils.SharedPreferencesMethods

object UtilThemeManager {

    fun applyTheme(context: Context) {
        val nightModeEnabled = SharedPreferencesMethods(context).getBooleanFromSP(AppPreferencesKeys.KEY_NIGHT_MODE, false)
        if (nightModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun isNightModeEnabled(context: Context): Boolean {
        return SharedPreferencesMethods(context).getBooleanFromSP(AppPreferencesKeys.KEY_NIGHT_MODE, false)
    }

    fun setNightModeEnabled(context: Context, enabled: Boolean) {
        SharedPreferencesMethods(context).setBooleanToSP(AppPreferencesKeys.KEY_NIGHT_MODE, enabled)
    }
}