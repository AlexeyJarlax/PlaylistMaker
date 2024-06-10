package com.practicum.playlistmaker.medialibrary.data.db.database

import androidx.room.Database
import androidx.room.RoomDatabase

import com.practicum.playlistmaker.medialibrary.data.db.dao.TrackDao
import com.practicum.playlistmaker.medialibrary.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class FavoritesTracksDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao
}