package com.practicum.playlistmaker.medialibrary.domain.others

import kotlinx.coroutines.flow.Flow

import com.practicum.playlistmaker.search.domain.models.Track

interface FavoritesTracksRepository {

    suspend fun addTrack(track: Track)
    suspend fun deleteTrack(track: Track)
    suspend fun deleteTrackById(trackId: Int)
    fun getTracksIDs():Flow<List<Int>>
    suspend fun getTrackById(trackId: Int): Track?
    fun getAllFavoritesTrack(): Flow<List<Track>>
}