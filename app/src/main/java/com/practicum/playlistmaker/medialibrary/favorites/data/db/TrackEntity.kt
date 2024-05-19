package com.practicum.playlistmaker.medialibrary.favorites.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks_table")
data class TrackEntity(
    @PrimaryKey val trackId: Int?,   // Id
    val trackName: String?,          // Название
    val artistName: String?,          // Исполнитель
    val trackTimeMillis: Long?,       // Продолжительность
    val artworkUrl100: String?,       // Пикча на обложку
    val collectionName: String?,      // Название альбома
    val releaseDate: String?,         // Год
    val primaryGenreName: String?,    // Жанр
    val country: String?,             // Страна
    val previewUrl: String?           // ссылка на 30 сек. фрагмент
)