package com.practicum.playlistmaker.medialibrary.ui.openplaylist

import android.net.Uri
import com.practicum.playlistmaker.search.domain.models.Track

sealed class OpenPlaylistState {
    data class Content(
        val imageUrl: Uri?,
        val playlistName: String,
        val playlistDetails: String?,
        val playlistDuration: String?,
        val playlistCountTrack: String?,
        val listTracks: List<Track>?
    ) : OpenPlaylistState()

    data object Delete : OpenPlaylistState()
}