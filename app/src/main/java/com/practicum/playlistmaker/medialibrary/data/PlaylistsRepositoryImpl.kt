package com.practicum.playlistmaker.medialibrary.data

import com.google.gson.Gson
import com.practicum.playlistmaker.medialibrary.data.db.AppDatabase
import com.practicum.playlistmaker.medialibrary.data.db.toPlaylist
import com.practicum.playlistmaker.medialibrary.data.db.toPlaylistEntity
import com.practicum.playlistmaker.medialibrary.data.db.toSelectedTrackEntity
import com.practicum.playlistmaker.medialibrary.domain.db.ImagesRepository
import com.practicum.playlistmaker.medialibrary.domain.db.Playlist
import com.practicum.playlistmaker.medialibrary.domain.db.PlaylistsRepository
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.toIntList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(
    private val db: AppDatabase,
    private val imagesRepository: ImagesRepository
) : PlaylistsRepository {

    override fun playlists(): Flow<List<Playlist>> {
        return db.playlistsDao().getAll().map { list ->
            list.map { it.toPlaylist(imagesRepository.filenameToUri(it.poster)) }
        }
    }

    override suspend fun getById(playlistId: Int): Playlist? {
        val playlist = db.playlistsDao().getById(playlistId)
        return playlist?.toPlaylist(imagesRepository.filenameToUri(playlist.poster))
    }

    override suspend fun upsertPlaylist(playlist: Playlist) {
        val filename = if (playlist.poster != null) { imagesRepository.save(playlist.poster) } else ""
        db.playlistsDao().upsert(playlist.toPlaylistEntity(filename))
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        db.playlistsDao().delete(playlistId)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        val tracks = playlist.tracks.toIntList()
        track.trackId?.let { tracks.add(it) }
        val newTracks = Gson().toJson(tracks)
        val newPlaylist = playlist.copy(tracks = newTracks, count = tracks.count())
        var filename = ""
        if (playlist.poster != null) filename = playlist.poster.toString().substringAfterLast('/')
        val newPlEntity = newPlaylist.toPlaylistEntity(filename)
        db.playlistsDao().upsert(newPlEntity)
        track.toSelectedTrackEntity()?.let { db.selectedTracksDao().insert(it) }
    }
}
