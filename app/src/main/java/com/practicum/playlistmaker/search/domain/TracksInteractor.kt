package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.models.TracksResponse
import androidx.core.util.Consumer

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: Consumer<TracksResponse>)
    fun saveToHistory(track: Track)
    fun loadFromHistory(): ArrayList<Track>
    fun killHistory()
    fun getActiveList(): ArrayList<Track>
}