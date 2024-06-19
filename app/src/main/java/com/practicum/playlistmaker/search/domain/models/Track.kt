package com.practicum.playlistmaker.search.domain.models

/* Модель данных: трек c возможностью упаковываться в джейсончики
   Объект содержит:
val trackId: Int?               // Id
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

import com.practicum.playlistmaker.utils.msToSs
import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Track(
    @SerialName("trackId") val trackId: Int?,
    @SerialName("trackName") val trackName: String?,
    @SerialName("artistName") val artistName: String?,
    @SerialName("trackTimeMillis") val trackTimeMillis: Long?,
    @SerialName("artworkUrl100") val artworkUrl100: String?,
    @SerialName("collectionName") val collectionName: String?,
    @SerialName("releaseDate") val releaseDate: String?,
    @SerialName("primaryGenreName") val primaryGenreName: String?,
    @SerialName("country") val country: String?,
    @SerialName("previewUrl") val previewUrl: String?
) : Serializable {
    val releaseYear: String?
        get() = releaseDate?.substringBefore('-')
    val trackTime: String
        get() = msToSs(trackTimeMillis)
}

