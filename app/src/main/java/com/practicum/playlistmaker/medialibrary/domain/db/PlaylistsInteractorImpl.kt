package com.practicum.playlistmaker.medialibrary.domain.db

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

}