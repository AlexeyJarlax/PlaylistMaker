package com.practicum.playlistmaker.medialibrary.ui.editplaylist

import android.net.Uri

sealed class EditPlaylistState {
    data class Content(
        val imageUrl: Uri?,
        val playlistName: String,
        val playlistDescription: String?
    ) : EditPlaylistState()
}