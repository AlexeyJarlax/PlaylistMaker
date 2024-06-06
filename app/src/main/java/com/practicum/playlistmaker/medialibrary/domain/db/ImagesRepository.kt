package com.practicum.playlistmaker.medialibrary.domain.db

import android.net.Uri

interface ImagesRepository {
    suspend fun save(uri: Uri): String
    suspend fun filenameToUri(filename: String): Uri?
}