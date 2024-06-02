package com.practicum.playlistmaker.medialibrary.playlists.data

import com.practicum.playlistmaker.medialibrary.playlists.data.db.Playlist
import com.practicum.playlistmaker.medialibrary.playlists.data.db.PlaylistDao


class PlaylistRepository(private val playlistDao: PlaylistDao) {

    fun addPlaylist(playlist: Playlist) {
        playlistDao.insertPlaylist(playlist)
    }

    fun getAllPlaylists(): List<Playlist> {
        return playlistDao.getAllPlaylists()
    }
}