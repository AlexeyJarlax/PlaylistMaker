package com.practicum.playlistmaker.garbage__data

import com.practicum.playlistmaker.garbage__data.dto.SearchRequestForTracksList
import com.practicum.playlistmaker.garbage__data.dto.SearchResponseForTracksList
import com.practicum.playlistmaker.garbage__domain.api.RepositoryForTracksList
import com.practicum.playlistmaker.garbage__domain.models.TracksList
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class RepositoryImplForTracksList(
    private val networkClientForTracksList: NetworkClientForTracksList
) : RepositoryForTracksList {

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
                        handleErrorResponse(1)
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
                handleErrorResponse(2)
                return emptyList()
            }
        } catch (e: Exception) {
            handleErrorResponse(3)
            return emptyList()
        }
    }

    private fun handleErrorResponse(resultCode: Int) {
        val error = when (resultCode) {
            1 -> "=== fun searchTracksList class RepositoryImplForTracksList => Ошибка при разборе даты"
            2 -> "=== fun searchTracksList class RepositoryImplForTracksList => Таких песен нет"
            else -> "=== Непредвиденная ошибка"
        }
        Timber.e(error)

    }
}
