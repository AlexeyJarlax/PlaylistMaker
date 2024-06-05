package com.practicum.playlistmaker.medialibrary.ui.playlists

import android.net.Uri

data class MLCreatePlaylistScreenState(
    val title: String = "",
    val description: String = "",
    val uri: Uri? = null,
    val isChangesExist: Boolean = false,
    val isTitleNotEmpty: Boolean = false
)