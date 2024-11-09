package com.practicum.playlistmaker.medialibrary.domain.others

import android.net.Uri

interface ImageStorage {

    fun saveImageToPrivateStorage(uri: Uri): String
    fun getImageFromPrivateStorage(imageName: String): Uri
}