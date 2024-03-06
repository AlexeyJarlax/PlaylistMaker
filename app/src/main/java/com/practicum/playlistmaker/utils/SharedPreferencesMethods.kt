package com.practicum.playlistmaker.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.AppPreferencesKeys

internal class SharedPreferencesMethods(private val context: Context) {
    private val sharedPreferences = getSharedPreferences()
    private fun getSharedPreferences() =
        context.getSharedPreferences(AppPreferencesKeys.PREFS_NAME, Context.MODE_PRIVATE)

    fun getBooleanFromSP(key: String, default: Boolean): Boolean {
        return sharedPreferences.getBoolean(
            key,
            default
        )
    }
}