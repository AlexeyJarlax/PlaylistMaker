package com.practicum.playlistmaker.medialibrary.ui.playlists

import com.practicum.playlistmaker.search.domain.models.Track

sealed class MLPlaylistsScreenState {

    data object Loading : MLPlaylistsScreenState()
    data class Ready(val historyList: List<Track>) : MLPlaylistsScreenState()
    data object Error : MLPlaylistsScreenState()
}