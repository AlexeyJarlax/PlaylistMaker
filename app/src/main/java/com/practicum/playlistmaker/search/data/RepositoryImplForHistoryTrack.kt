package com.practicum.playlistmaker.search.data

import android.content.SharedPreferences
import com.practicum.playlistmaker.search.domain.TracksHistoryRepository
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.AppPreferencesKeys.SEARCH_HISTORY
import com.google.gson.Gson

class RepositoryImplForHistoryTrack(private val sharedPreferences: SharedPreferences) :
    TracksHistoryRepository {
    override fun saveToHistory(history: ArrayList<Track>) {
        val json = Gson().toJson(history)
        sharedPreferences.edit().putString(SEARCH_HISTORY, json).apply()
    }

    override fun loadFromHistory(): ArrayList<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY, null) ?: return arrayListOf()
        return ArrayList(Gson().fromJson(json, Array<Track>::class.java).toList())
    }

    override fun killHistory() {
        sharedPreferences.edit().remove(SEARCH_HISTORY).apply()
    }

}