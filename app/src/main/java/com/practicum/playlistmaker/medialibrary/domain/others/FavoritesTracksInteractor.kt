package com.practicum.playlistmaker.medialibrary.domain.others

import kotlinx.coroutines.flow.Flow

import com.practicum.playlistmaker.search.domain.models.Track

interface FavoritesTracksInteractor {

    fun getAllTracksSortedById(): Flow<List<Track>>
    suspend fun addTrack(track: Track)
    suspend fun deleteTrack(track: Track)
    fun getTracksIDs(): Flow<List<Int>>
//    suspend fun upsertTrack(track: Track)
}