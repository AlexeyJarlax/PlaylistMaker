package com.practicum.playlistmaker.data.dto

// ITunesTrack - DTO модель данных для преобразования ответа iTunes Search API в список объектов Track, запрашивается через TrackResponse

data class ITunesForTracksList(
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: Long?,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?
)