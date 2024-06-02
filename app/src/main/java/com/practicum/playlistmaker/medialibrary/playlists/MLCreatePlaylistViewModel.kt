package com.practicum.playlistmaker.medialibrary.playlists

import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.medialibrary.playlists.data.PlaylistRepository
import com.practicum.playlistmaker.medialibrary.playlists.data.db.Playlist


class MLCreatePlaylistViewModel(private val repository: PlaylistRepository) : ViewModel() {

    fun createPlaylist(title: String, description: String, coverPath: String?) {
        val playlist = Playlist(
            title = title,
            description = description,
            coverPath = coverPath ?: "",
            trackIds = "",
            trackCount = 0
        )
        repository.addPlaylist(playlist)
    }
}