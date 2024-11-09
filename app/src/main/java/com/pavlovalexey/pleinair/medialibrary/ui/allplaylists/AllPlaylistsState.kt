package com.practicum.playlistmaker.medialibrary.ui.allplaylists

import com.practicum.playlistmaker.medialibrary.domain.model.Playlist

sealed interface AllPlaylistsState {
    data class Content(
        val playList: List<Playlist>
    ) : AllPlaylistsState

    data class Empty(val playList: List<Playlist>) : AllPlaylistsState
}