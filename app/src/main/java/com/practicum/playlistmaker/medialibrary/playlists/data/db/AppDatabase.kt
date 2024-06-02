package com.practicum.playlistmaker.medialibrary.playlists.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Playlist::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
}