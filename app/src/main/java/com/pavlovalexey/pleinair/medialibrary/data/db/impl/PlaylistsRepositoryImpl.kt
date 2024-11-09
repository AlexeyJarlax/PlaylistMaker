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

    override suspend fun addTrackInPlaylist(track: Track) {
        playlistDb.trackPlaylistDao().insertTrack(converter.trackToTrackPlaylistEntity(track))
    }

    override suspend fun getPlaylistById(playlistId: Int): Playlist {
        return converter.playlistEntityToPlaylist(
            playlistDb.playlistDao().getPlaylistById(playlistId)
        )
    }

    override suspend fun updatePlaylistAndAddTrack(track: Track, playlist: Playlist) {
        track.trackId?.let { playlist.tracksIds.add(it) }
        playlist.tracksCount += 1
        playlistDb.playlistDao()
            .updatePlaylist(
                converter.playlistToPlaylistEntity(playlist)
            )
        addTrackInPlaylist(track)
    }

    override suspend fun updatePlaylistAndDeleteTrack(trackId: Int, playlist: Playlist) {
        playlist.tracksIds.remove(trackId)
        playlist.tracksCount = playlist.tracksIds.size
        playlistDb.playlistDao().updatePlaylist(
            converter.playlistToPlaylistEntity(playlist)
        )
        val list = mutableListOf<Int>()
        playlistDb.playlistDao().getAllTracksOfPlaylists()
            .map {
                list.addAll(converter.idsStringToList(it))
            }
        if (!list.contains(trackId)) {
            playlistDb.trackPlaylistDao()
                .deleteTrackFromPlaylistById(trackId)
        }
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistDb.playlistDao().updatePlaylist(converter.playlistToPlaylistEntity(playlist))
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        playlistDb.playlistDao().deletePlaylist(playlistId)
        deleteUnusedTracks()
    }

    override suspend fun getTrackById(trackId: Int): Track {
        return converter.trackPlaylistEntityToTrack(
            playlistDb.trackPlaylistDao().getTrackById(trackId)
        )
    }

    private suspend fun deleteUnusedTracks() {
        val listAllTracksInPlaylists: MutableList<Int> = mutableListOf()
        playlistDb.playlistDao().getAllTracksOfPlaylists().map { res ->
            listAllTracksInPlaylists.addAll(converter.idsStringToList(res))
        }
        val listTrack = playlistDb.trackPlaylistDao().getAllIdsTrack()
        listTrack.map {
            if (!listAllTracksInPlaylists.contains(it)) {
                playlistDb.trackPlaylistDao().deleteTrackFromPlaylistById(it)
            }
        }
    }
}