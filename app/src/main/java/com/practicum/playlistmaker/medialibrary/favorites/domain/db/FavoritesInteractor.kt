package com.practicum.playlistmaker.medialibrary.favorites.domain.db

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    fun getAllTracksSortedById(): Flow<List<Track>>
    fun getTrackIds(): Flow<List<Int>>
    suspend fun upsertTrack(track: Track)
}