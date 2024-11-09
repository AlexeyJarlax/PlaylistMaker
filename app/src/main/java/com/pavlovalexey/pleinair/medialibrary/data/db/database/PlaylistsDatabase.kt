package com.practicum.playlistmaker.medialibrary.data.db.database

import androidx.room.Database
import androidx.room.RoomDatabase

import com.practicum.playlistmaker.medialibrary.data.db.dao.PlaylistDao
import com.practicum.playlistmaker.medialibrary.data.db.dao.TrackAndPlaylistDao
import com.practicum.playlistmaker.medialibrary.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.medialibrary.data.db.entity.TrackAndPlaylistEntity

@Database(version = 1, entities = [PlaylistEntity::class, TrackAndPlaylistEntity::class])
abstract class PlaylistsDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao

    abstract fun trackPlaylistDao(): TrackAndPlaylistDao
}