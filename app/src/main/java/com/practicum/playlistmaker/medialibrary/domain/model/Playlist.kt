package com.practicum.playlistmaker.medialibrary.domain.model

import android.net.Uri

data class Playlist(
    val id: Int,
    var playlistName: String,
    var description: String,
    var urlImage: Uri?,
    var tracksIds: MutableList<Int>,
    var tracksCount: Int
)