package com.practicum.playlistmaker.medialibrary.favorites

import com.practicum.playlistmaker.search.domain.models.Track

sealed class MLFavoritesScreenState {

    data object Loading : MLFavoritesScreenState()
    data class Ready(val historyList: List<Track>) : MLFavoritesScreenState()
    data object Error : MLFavoritesScreenState()
}