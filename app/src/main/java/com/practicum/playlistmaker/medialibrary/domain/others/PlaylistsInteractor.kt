package com.practicum.playlistmaker.medialibrary.domain.others

import kotlinx.coroutines.flow.Flow
import android.net.Uri

import com.practicum.playlistmaker.medialibrary.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.models.Track

interface PlaylistsInteractor {

    suspend fun addPlaylist(playlist: Playlist)
    fun getAllPlaylists(): Flow<List<Playlist>>
    suspend fun updatePlaylist(track: Track, playlist: Playlist)
    suspend fun addTrackInPlaylist(track: Track)
    fun saveImageToPrivateStorage(uri: Uri): String
    fun getImageFromPrivateStorage(imageName: String): Uri
}