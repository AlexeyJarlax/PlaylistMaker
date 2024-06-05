package com.practicum.playlistmaker.medialibrary.domain.db

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getAllTracksSortedById(): Flow<List<Track>>
    fun getTrackIds(): Flow<List<Int>>
    suspend fun getTrackById(trackId: Int): Track?
    suspend fun upsertTrack(track: Track)
    suspend fun deleteTrackById(trackId: Int)
}