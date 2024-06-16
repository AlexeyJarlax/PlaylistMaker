package com.practicum.playlistmaker.medialibrary.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_in_playlist")
data class TrackAndPlaylistEntity(
    @PrimaryKey
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val collectionName: String,
    val country: String,
    val previewUrl: String
)