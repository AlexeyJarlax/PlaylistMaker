package com.practicum.playlistmaker.medialibrary.domain.db

import android.net.Uri

data class Playlist(
    val id: Int?,
    val title: String,
    val description: String?,
    val poster: Uri?,
    val count: Int
)