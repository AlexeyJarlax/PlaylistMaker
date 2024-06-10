package com.practicum.playlistmaker.medialibrary.ui.favorites

import com.practicum.playlistmaker.search.domain.models.Track

sealed interface FavoriteState {
    data class Content(
        val tracks: List<Track>
    ) : FavoriteState

    data object Empty : FavoriteState
}