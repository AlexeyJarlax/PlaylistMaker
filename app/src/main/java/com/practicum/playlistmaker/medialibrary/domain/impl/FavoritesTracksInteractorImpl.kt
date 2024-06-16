package com.practicum.playlistmaker.medialibrary.domain.impl

import android.util.Log
import kotlinx.coroutines.flow.Flow

import com.practicum.playlistmaker.medialibrary.domain.others.FavoritesTracksInteractor
import com.practicum.playlistmaker.medialibrary.domain.others.FavoritesTracksRepository
import com.practicum.playlistmaker.search.domain.models.Track


class FavoritesTracksInteractorImpl(private val repository: FavoritesTracksRepository) :
    FavoritesTracksInteractor {

    override fun getAllTracksSortedById(): Flow<List<Track>> {
        return repository.getAllFavoritesTrack()
    }

    override suspend fun addTrack(track: Track) {
        Log.d("=== LOG ===", "=== FavoritesInteractorImpl > changeFavorite(track: Track)")
        if (track.trackId?.let { repository.getTrackById(it) } == null) {
            repository.addTrack(track)
    }}

    override suspend fun deleteTrack(track: Track) {
        repository.deleteTrack(track)
    }

    override suspend fun deleteTrackById(trackId: Int) {
        repository.deleteTrackById(trackId)
    }

    override fun getTracksIDs(): Flow<List<Int>> {
        return repository.getTracksIDs()
    }
}