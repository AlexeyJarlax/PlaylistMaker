package com.practicum.playlistmaker.medialibrary.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(version = 2, entities = [TrackEntity::class, PlaylistEntity::class, SelectedTrackEntity::class], exportSchema = false)
@TypeConverters(UriTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoritesDao(): TrackDao
    abstract fun playlistsDao(): PlaylistsDao
    abstract fun selectedTracksDao(): SelectedTracksDao
}
