package com.practicum.playlistmaker.medialibrary.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val playlistName: String,
    val description: String,
    val urlImage: String,
    val tracksIds: String,
    val tracksCount: Int
)