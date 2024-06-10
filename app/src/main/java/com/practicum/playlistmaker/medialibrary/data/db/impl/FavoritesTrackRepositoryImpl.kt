package com.practicum.playlistmaker.medialibrary.data.db.impl

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

import com.practicum.playlistmaker.medialibrary.data.db.database.FavoritesTracksDatabase
import com.practicum.playlistmaker.medialibrary.data.db.converters.TrackDbConverter
import com.practicum.playlistmaker.medialibrary.domain.others.FavoritesTracksRepository
import com.practicum.playlistmaker.search.domain.models.Track

class FavoritesTrackRepositoryImpl(
    private val database: FavoritesTracksDatabase,
    private val converter: TrackDbConverter
) : FavoritesTracksRepository {

    override suspend fun addTrack(track: Track) {
        database.trackDao().insertTrack(
            converter.trackToTrackEntity(
                track
            )
        )
    }

    override suspend fun deleteTrack(track: Track) {
        track.trackId?.let { database.trackDao().deleteTrack(it) }
    }

    override fun getTracksIDs(): Flow<List<Int>> {
        return database.trackDao().getIdsTracks()
    }

    override fun getAllFavoritesTrack(): Flow<List<Track>> {
        return database.trackDao().getAllTrack().map { result ->
            result.map { converter.trackEntityToTrack(it) }
        }
    }
//    override suspend fun upsertTrack(track: Track) {
//        database.trackDao().upsertTrack(converter.trackToTrackEntity(track))
//        Log.d("=== LOG ===", "=== FavoritesRepositoryImpl > upsertTrack")
//    }
}






