package com.practicum.playlistmaker.medialibrary.ui.playlists

import com.practicum.playlistmaker.medialibrary.domain.db.Playlist
import com.practicum.playlistmaker.search.domain.models.Track

sealed class MLPlaylistsScreenState {

    data object Loading : MLPlaylistsScreenState()
    data class Content(val playlistsList: List<Playlist>) : MLPlaylistsScreenState()
    data object Error : MLPlaylistsScreenState()
}