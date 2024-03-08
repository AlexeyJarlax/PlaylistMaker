package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.domain.models.Track

interface TracksHistoryRepository {
    fun saveToHistory(history: ArrayList<Track>)
    fun loadFromHistory(): ArrayList<Track>
    fun killHistory()
}