package com.practicum.playlistmaker.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.garbage__domain.models.TracksList
import com.practicum.playlistmaker.utils.AppPreferencesKeys

internal class SharedPreferencesMethods(private val context: Context) {
    private val sharedPreferences = getSharedPreferences()
    private fun getSharedPreferences() =
        context.getSharedPreferences(AppPreferencesKeys.PREFS_NAME, Context.MODE_PRIVATE)

    // ------------------------------------------------------ json TrackList Object гетеры и сетеры
    fun getTrackListFromSP(): MutableList<TracksList> {
        val jsonString = sharedPreferences.getString(AppPreferencesKeys.KEY_HISTORY_LIST, null)
        val type = object : TypeToken<List<TracksList>>() {}.type
        return Gson().fromJson(jsonString, type) ?: mutableListOf()
    }

    fun saveTrackListToSP(trackList: List<TracksList>) {
        val jsonString = Gson().toJson(trackList)
        sharedPreferences.edit().putString(AppPreferencesKeys.KEY_HISTORY_LIST, jsonString).apply()
    }

    fun doesHistoryListExists(): Boolean {
        return sharedPreferences.contains(AppPreferencesKeys.KEY_HISTORY_LIST)
    }

    // ---------------------------------------------------------------------- удаление любой строки
    fun delFromSP(key: String) {
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }

    // --------------------------------------------------------------------- Boolean гетеры и сетеры
    fun setBooleanToSP(key: String, isChecked: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, isChecked)
        editor.apply()
    }

    fun getBooleanFromSP(key: String, default: Boolean): Boolean {
        return sharedPreferences.getBoolean(
            key,
            default
        )
    }
}