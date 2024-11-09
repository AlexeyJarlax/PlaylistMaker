package com.practicum.playlistmaker.medialibrary.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_table")
data class TrackEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?, // Id DB
    val trackId: Int?,              // Id трека
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