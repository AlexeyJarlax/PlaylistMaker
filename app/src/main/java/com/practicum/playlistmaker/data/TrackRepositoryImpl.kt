package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.data.dto.TrackSearchResponse
import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.domain.models.Track
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    override fun searchTrack(query: String): List<Track> {
        val response = networkClient.doRequest(TrackSearchRequest(query))
        if (response.resultCode == 200) {
            return (response as TrackSearchResponse).results.map { track ->
                val releaseDateTime = try {
                    track.releaseDate?.let {
                        LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME)
                    } ?: LocalDateTime.MIN
                } catch (e: DateTimeParseException) {
                    Timber.e(e, "=== fun searchMovies class MoviesRepositoryImpl => Ошибка при разборе даты: ${track.releaseDate}")
                    LocalDateTime.MIN
                }
                Track(
                    track.trackName ?: "",
                    track.artistName ?: "",
                    track.trackTimeMillis ?: 0,
                    track.artworkUrl100 ?: "",
                    track.collectionName ?: "",
                    releaseDateTime.year.toString(),
                    track.primaryGenreName ?: "",
                    track.country ?: "",
                    track.previewUrl ?: ""
                )
            }
        } else {
            Timber.d("=== fun searchMovies class MoviesRepositoryImpl => emptyList() таких песен нет")
            return emptyList()
        }
    }
}