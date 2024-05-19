package com.practicum.playlistmaker.medialibrary.favorites.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.medialibrary.favorites.data.db.TrackDao
import com.practicum.playlistmaker.medialibrary.favorites.data.db.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
}