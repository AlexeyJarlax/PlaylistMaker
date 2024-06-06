package com.practicum.playlistmaker.medialibrary.data

import android.util.Log
import com.practicum.playlistmaker.medialibrary.data.db.AppDatabase
import com.practicum.playlistmaker.medialibrary.data.db.toTrack
import com.practicum.playlistmaker.medialibrary.data.db.toTrackEntity
import com.practicum.playlistmaker.medialibrary.domain.db.FavoritesRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(private val db: AppDatabase) : FavoritesRepository {

    override fun getAllTracksSortedById(): Flow<List<Track>> {
        Log.d("=== LOG ===", "=== FavoritesRepositoryImpl > getAllTracksSortedById")
        return db.favoritesDao().getAllTracksSortedById().map { list ->
            list.map { it.toTrack() }
        }
    }

    override fun getTrackIds(): Flow<List<Int>> {
        Log.d("=== LOG ===", "=== FavoritesRepositoryImpl > getTrackIds")
        return db.favoritesDao().getTrackIds()
    }

    override suspend fun getTrackById(trackId: Int): Track? {
        Log.d("=== LOG ===", "=== FavoritesRepositoryImpl > getTrackById")
        return db.favoritesDao().getTrackById(trackId)?.toTrack()
    }

    override suspend fun upsertTrack(track: Track) {
        db.favoritesDao().upsertTrack(track.toTrackEntity())
        Log.d("=== LOG ===", "=== FavoritesRepositoryImpl > upsertTrack")
    }

    override suspend fun deleteTrackById(trackId: Int) {
        db.favoritesDao().deleteTrackById(trackId)
        Log.d("=== LOG ===", "=== FavoritesRepositoryImpl > deleteTrackById")
    }
}
