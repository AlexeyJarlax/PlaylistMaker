package com.practicum.playlistmaker.medialibrary.favorites.domain.db

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun favoriteTracks(): Flow<List<Track>>
    suspend fun deleteTrack(trackId: Int)
}