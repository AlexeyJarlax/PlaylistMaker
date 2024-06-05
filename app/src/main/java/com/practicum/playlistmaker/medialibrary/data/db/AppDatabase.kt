package com.practicum.playlistmaker.medialibrary.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(version = 1, entities = [TrackEntity::class, PlaylistEntity::class], exportSchema = false)
@TypeConverters(UriTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoritesDao(): TrackDao
    abstract fun playlistsDao(): PlaylistsDao
}