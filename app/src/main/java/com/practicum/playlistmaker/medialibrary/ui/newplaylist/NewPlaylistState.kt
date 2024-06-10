package com.practicum.playlistmaker.medialibrary.ui.newplaylist

sealed interface NewPlaylistState {

    data object Success : NewPlaylistState
    data object Error : NewPlaylistState
}