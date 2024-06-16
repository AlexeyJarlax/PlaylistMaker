package com.practicum.playlistmaker.medialibrary.ui.playlist

import com.practicum.playlistmaker.medialibrary.domain.model.Playlist

sealed interface PlaylistState {
    data class Content(
        val playList: List<Playlist>
    ) : PlaylistState

    data object Empty : PlaylistState
}