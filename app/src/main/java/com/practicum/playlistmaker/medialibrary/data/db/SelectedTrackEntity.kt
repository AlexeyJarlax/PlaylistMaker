package com.practicum.playlistmaker.medialibrary.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "selected_tracks_table")
data class SelectedTrackEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
)