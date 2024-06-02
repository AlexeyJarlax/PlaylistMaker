package com.practicum.playlistmaker.medialibrary.playlists.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class Playlist(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val coverPath: String,
    val trackIds: String,
    val trackCount: Int
)