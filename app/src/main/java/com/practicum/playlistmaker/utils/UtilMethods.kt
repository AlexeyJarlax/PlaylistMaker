package com.practicum.playlistmaker.utils

import android.content.Context
import java.text.SimpleDateFormat
import java.util.Locale

internal class UtilMethods(private val context: Context) {
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

internal fun mmss(ms: Long?): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(ms)