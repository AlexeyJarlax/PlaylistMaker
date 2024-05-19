package com.practicum.playlistmaker.medialibrary.favorites.data

import com.practicum.playlistmaker.medialibrary.favorites.data.db.AppDatabase
import com.practicum.playlistmaker.medialibrary.favorites.domain.db.FavoritesRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(private val appDatabase: AppDatabase, private val trackDbConvertor: TrackDbConvertor) :
    FavoritesRepository {

    override fun historyMovies(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getHistory()
        emit(convertFromEntity(tracks))
    }

    private fun convertFromEntity(track: Flow<List<Track>>): List<Track> {
        return track.map { track -> trackDbConvertor.map(track) }
    }

}