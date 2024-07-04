package com.practicum.playlistmaker.medialibrary.ui.favorites

import com.practicum.playlistmaker.search.domain.models.Track

sealed class FavoritesState {
    data object Loading : FavoritesState()
    data class Ready(val favoritesList: List<Track>) : FavoritesState()
    data object Error : FavoritesState()
}