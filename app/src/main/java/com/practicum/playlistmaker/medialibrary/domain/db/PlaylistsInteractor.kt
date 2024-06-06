package com.practicum.playlistmaker.medialibrary.domain.db

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    fun playlists(): Flow<List<Playlist>>
    suspend fun upsertPlaylist(playlist: Playlist)
    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)
}