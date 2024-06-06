package com.practicum.playlistmaker.medialibrary.domain.db

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    fun playlists(): Flow<List<Playlist>>
    suspend fun getById(playlistId: Int): Playlist?
    suspend fun upsertPlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlistId: Int)
    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)
}