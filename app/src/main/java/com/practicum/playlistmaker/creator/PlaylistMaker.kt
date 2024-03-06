package com.practicum.playlistmaker.creator

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.utils.AppPreferencesKeys
import com.practicum.playlistmaker.utils.SharedPreferencesMethods

class PlaylistMaker : Application()  {
    override fun onCreate() {
        super.onCreate()
        applyTheme(this)
    }

    private fun applyTheme(context: Context) {
        val nightModeEnabled = SharedPreferencesMethods(context).getBooleanFromSP(
            AppPreferencesKeys.KEY_NIGHT_MODE, false)
        if (nightModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}