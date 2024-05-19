package com.practicum.playlistmaker.medialibrary.favorites.domain.db

import com.practicum.playlistmaker.medialibrary.favorites.data.db.TrackDao
import com.practicum.playlistmaker.medialibrary.favorites.data.db.TrackEntity
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow


interface FavoritesRepository {
    fun historyMovies(): Flow<List<Track>>
}