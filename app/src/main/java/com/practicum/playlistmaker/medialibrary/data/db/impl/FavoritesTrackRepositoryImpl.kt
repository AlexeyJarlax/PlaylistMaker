package com.practicum.playlistmaker.medialibrary.data.db.impl

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

import com.practicum.playlistmaker.medialibrary.data.db.database.FavoritesTracksDatabase
import com.practicum.playlistmaker.medialibrary.data.db.converters.toTrack
import com.practicum.playlistmaker.medialibrary.data.db.converters.toTrackEntity
import com.practicum.playlistmaker.medialibrary.domain.others.FavoritesTracksRepository
import com.practicum.playlistmaker.search.domain.models.Track

class FavoritesTrackRepositoryImpl(
    private val database: FavoritesTracksDatabase,
) : FavoritesTracksRepository {

    override suspend fun addTrack(track: Track) {
        database.trackDao().insertTrack(
            track.toTrackEntity()
        )
    }

    override suspend fun deleteTrack(track: Track) {
        track.trackId?.let { database.trackDao().deleteTrack(it) }
    }

    override suspend fun deleteTrackById(trackId: Int) {
        database.trackDao().deleteTrackById(trackId)
        Log.d("=== LOG ===", "=== FavoritesRepositoryImpl > deleteTrackById")
    }

    override fun getTracksIDs(): Flow<List<Int>> {
        return database.trackDao().getIdsTracks()
    }

    override suspend fun getTrackById(trackId: Int): Track? {
        Log.d("=== LOG ===", "=== FavoritesRepositoryImpl > getTrackById")
        return database.trackDao().getTrackById(trackId)?.toTrack()
    }

    override fun getAllFavoritesTrack(): Flow<List<Track>> {
        return database.trackDao().getAllTrack().map { result ->
            result.map { it.toTrack() }
        }
    }
}






