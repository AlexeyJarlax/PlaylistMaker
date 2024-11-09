package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.models.TracksResponse
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun searchTracks(expression: String): Flow<TracksResponse>
    fun saveToHistory(track: Track)
    fun loadFromHistory(): ArrayList<Track>
    fun killHistory()
    fun getActiveList(): ArrayList<Track>
}