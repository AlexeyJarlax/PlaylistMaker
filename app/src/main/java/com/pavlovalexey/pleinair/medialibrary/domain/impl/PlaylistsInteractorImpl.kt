package com.practicum.playlistmaker.medialibrary.domain.impl

import kotlinx.coroutines.flow.Flow
import android.net.Uri

import com.practicum.playlistmaker.medialibrary.domain.others.ImageStorage
import com.practicum.playlistmaker.medialibrary.domain.others.PlaylistsInteractor
import com.practicum.playlistmaker.medialibrary.domain.others.PlaylistsRepository
import com.practicum.playlistmaker.medialibrary.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.models.Track

class PlaylistInteractorImpl(
    private val repository: PlaylistsRepository,
    private val imageStorage: ImageStorage
) : PlaylistsInteractor {
    override suspend fun addPlaylist(playlist: Playlist) {
        repository.addPlaylist(playlist)
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return repository.getAllPlaylists()
    }

    override suspend fun addTrackInPlaylist(track: Track) {
        repository.addTrackInPlaylist(track)
    }

    override fun saveImageToPrivateStorage(uri: Uri): String {
        return imageStorage.saveImageToPrivateStorage(uri)
    }

    override fun getImageFromPrivateStorage(imageName: String): Uri {
        return imageStorage.getImageFromPrivateStorage(imageName)
    }

    override suspend fun getPlaylistById(playlistId: Int): Playlist {
        return repository.getPlaylistById(playlistId)
    }

    override suspend fun updatePlaylistAndAddTrack(track: Track, playlist: Playlist) {
        repository.updatePlaylistAndAddTrack(track, playlist)
    }

    override suspend fun updatePlaylistAndDeleteTrack(trackId: Int, playlist: Playlist) {
        repository.updatePlaylistAndDeleteTrack(trackId, playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        repository.updatePlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        repository.deletePlaylist(playlistId)
    }

    override suspend fun getTrackById(trackId: Int): Track {
        return repository.getTrackById(trackId)
    }
}