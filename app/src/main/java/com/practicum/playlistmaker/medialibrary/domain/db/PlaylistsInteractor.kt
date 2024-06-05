package com.practicum.playlistmaker.medialibrary.domain.db

import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    fun playlists(): Flow<List<Playlist>>

    suspend fun upsertPlaylist(playlist: Playlist)
}