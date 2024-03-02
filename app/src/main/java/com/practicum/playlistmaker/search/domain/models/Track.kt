package com.practicum.playlistmaker.search.domain.models

import kotlinx.serialization.SerialName
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

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
    val bigCoverUrl: String?
        get() = artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")
    val releaseYear: String?
        get() = releaseDate?.substringBefore('-')
    val trackTime: String
        get() = mmss(trackTimeMillis)
}

fun mmss(ms: Long?): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(ms)