package com.practicum.playlistmaker.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/* Модель данных: трек c возможностью упаковываться в джейсончики
   Объект содержит:
val trackName: String?          // Название
val artistName: String?         // Исполнитель
val trackTimeMillis: Long?      // Продолжительность
val artworkUrl100: String?      // Пикча на обложку
val collectionName: String?     // Название альбома
val releaseDate: String?        // Год
val primaryGenreName: String?   // Жанр
val country: String?            // Страна
val previewUrl: String?         // ссылка на 30 сек. фрагмент
*/

@Serializable
data class TracksList(
    @SerialName("trackName") val trackName: String?,
    @SerialName("artistName") val artistName: String?,
    @SerialName("trackTimeMillis") val trackTimeMillis: Long?,
    @SerialName("artworkUrl100") val artworkUrl100: String?,
    @SerialName("collectionName") val collectionName: String?,
    @SerialName("releaseDate") val releaseDate: String?,
    @SerialName("primaryGenreName") val primaryGenreName: String?,
    @SerialName("country") val country: String?,
    @SerialName("previewUrl") val previewUrl: String?
) {
    fun toTrackData() = TracksList(
        trackName,
        artistName,
        trackTimeMillis,
        artworkUrl100,
        collectionName,
        releaseDate,
        primaryGenreName,
        country,
        previewUrl
    )
}