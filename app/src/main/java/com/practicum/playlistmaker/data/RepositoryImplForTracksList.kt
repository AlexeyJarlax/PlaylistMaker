package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.dto.SearchRequestForTracksList
import com.practicum.playlistmaker.data.dto.SearchResponseForTracksList
import com.practicum.playlistmaker.domain.api.RepositoryForTracksList
import com.practicum.playlistmaker.domain.models.TracksList
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class RepositoryImplForTracksList(private val networkClientForTracksList: NetworkClientForTracksList) : RepositoryForTracksList {

    override fun searchTracksList(query: String): List<TracksList> {
        try {
            val response = networkClientForTracksList.doRequest(SearchRequestForTracksList(query))

            if (response.resultCode == 200) {
                return (response as SearchResponseForTracksList).results.map { track ->
                    val releaseDateTime = try {
                        track.releaseDate?.let {
                            LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME)
                        } ?: LocalDateTime.MIN
                    } catch (e: DateTimeParseException) {
                        Timber.e(e, "=== fun searchTrack, class TrackRepositoryImpl => Ошибка при разборе даты: ${track.releaseDate}")
                        LocalDateTime.MIN
                    }
                    TracksList(
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
                handleErrorResponse(response.resultCode)
                return emptyList()
            }
        } catch (e: Exception) {
            Timber.e(e, "=== fun searchMovies class MoviesRepositoryImpl => непредвиденная ошибка")
            return emptyList()
        }
    }

    private fun handleErrorResponse(resultCode: Int) {
        val error = when (resultCode) {
            400 -> "=== 400 Bad Request"
            401 -> "=== 401 Unauthorized"
            403 -> "=== 403 Forbidden"
            404 -> "=== 404 Not Found"
            500 -> "=== 500 Internal Server Error"
            503 -> "=== 503 Service Unavailable"
            else -> "=== Unknown Error"
        }
        Timber.e(error)
    }
}