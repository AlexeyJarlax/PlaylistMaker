package com.practicum.playlistmaker.medialibrary.domain.db

import android.util.Log
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(
    private val favoritesRepository: FavoritesRepository
) : FavoritesInteractor {

    override fun getAllTracksSortedById(): Flow<List<Track>> {
        return favoritesRepository.getAllTracksSortedById()
    }

    override fun getTrackIds(): Flow<List<Int>> {
        return favoritesRepository.getTrackIds()
    }

    override suspend fun upsertTrack(track: Track) {
        Log.d("=== LOG ===", "=== FavoritesInteractorImpl > changeFavorite(track: Track)")
        if (track.trackId?.let { favoritesRepository.getTrackById(it) } == null) {
            favoritesRepository.upsertTrack(track)
        } else favoritesRepository.deleteTrackById(track.trackId)
    }

}