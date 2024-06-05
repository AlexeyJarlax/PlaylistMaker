package com.practicum.playlistmaker.medialibrary.data

import com.practicum.playlistmaker.medialibrary.data.db.AppDatabase
import com.practicum.playlistmaker.medialibrary.data.db.toPlaylist
import com.practicum.playlistmaker.medialibrary.data.db.toPlaylistEntity
import com.practicum.playlistmaker.medialibrary.domain.db.Playlist
import com.practicum.playlistmaker.medialibrary.domain.db.PlaylistsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(private val db: AppDatabase) : PlaylistsRepository {

    override fun playlists(): Flow<List<Playlist>> {
        return db.playlistsDao().getAll().map { list ->
            list.map { it.toPlaylist() }
        }
    }

    override suspend fun getById(playlistId: Int): Playlist? {
        return db.playlistsDao().getById(playlistId)?.toPlaylist()
    }

    override suspend fun upsertPlaylist(playlist: Playlist) {
        db.playlistsDao().upsert(playlist.toPlaylistEntity())
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        db.playlistsDao().delete(playlistId)
    }

}