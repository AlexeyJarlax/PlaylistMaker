package com.practicum.playlistmaker.medialibrary.data.db.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

import com.practicum.playlistmaker.medialibrary.data.db.converters.PlaylistDbConverter
import com.practicum.playlistmaker.medialibrary.data.db.database.PlaylistsDatabase
import com.practicum.playlistmaker.medialibrary.domain.others.PlaylistsRepository
import com.practicum.playlistmaker.medialibrary.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.models.Track

class PlaylistsRepositoryImpl(
    private val playlistDb: PlaylistsDatabase,
    private val converter: PlaylistDbConverter
) : PlaylistsRepository {
    override suspend fun addPlaylist(playlist: Playlist) {
        playlistDb.playlistDao().insertPlaylist(converter.playlistToPlaylistEntity(playlist))
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistDb.playlistDao().getAllPlaylist().map { listPlaylists ->
            listPlaylists.map { playlist ->
                converter.playlistEntityToPlaylist(playlist)
            }
        }
    }

    override suspend fun updatePlaylist(track: Track, playlist: Playlist) {
        track.trackId?.let { playlist.tracksIds.add(it) }
        playlist.tracksCount += 1

        playlistDb.playlistDao()
            .updatePlaylist(
                converter.playlistToPlaylistEntity(playlist)
            )
        addTrackInPlaylist(track)
    }

    override suspend fun addTrackInPlaylist(track: Track) {
        playlistDb.trackPlaylistDao().insertTrack(converter.trackToTrackPlaylistEntity(track))
    }
}