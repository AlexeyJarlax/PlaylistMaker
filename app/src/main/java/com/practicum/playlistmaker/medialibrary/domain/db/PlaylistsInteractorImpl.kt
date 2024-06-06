package com.practicum.playlistmaker.medialibrary.domain.db

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(
    private val playlistsRepository: PlaylistsRepository
) : PlaylistsInteractor {

    override fun playlists(): Flow<List<Playlist>> {
        return playlistsRepository.playlists()
    }

    override suspend fun upsertPlaylist(playlist: Playlist) {
        playlistsRepository.upsertPlaylist(playlist)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        playlistsRepository.addTrackToPlaylist(track, playlist)
    }
}