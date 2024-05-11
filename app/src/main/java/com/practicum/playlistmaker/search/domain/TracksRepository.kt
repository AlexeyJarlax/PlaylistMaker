package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.models.TracksResponse
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun saveToHistory(history: ArrayList<Track>)
    fun loadFromHistory(): ArrayList<Track>
    fun killHistory()
    fun searchTracks(expression: String): Flow<TracksResponse>
}