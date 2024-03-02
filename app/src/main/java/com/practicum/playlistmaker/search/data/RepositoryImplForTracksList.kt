package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.data.network.NetworkClientForTracksList
import com.practicum.playlistmaker.search.data.dto.SearchRequestForTracksList
import com.practicum.playlistmaker.search.data.dto.SearchResponseForTracksList
import com.practicum.playlistmaker.search.domain.TracksSearchRepository
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.models.TracksResponse
import timber.log.Timber

class RepositoryImplForTracksList(private val networkClientForTracksList: NetworkClientForTracksList): TracksSearchRepository {

    override fun searchTracks(expression: String): TracksResponse {
        val response = networkClientForTracksList.doRequest(SearchRequestForTracksList(expression))
        if (response.resultCode == 200) {
            val trackList = (response as SearchResponseForTracksList).results.map {
                Track(
                    trackId = it.trackId,
                    trackName = it.trackName ?: "",
                    artistName = it.artistName ?: "",
                    trackTimeMillis = it.trackTimeMillis ?: 0L,
                    artworkUrl100 = it.artworkUrl100 ?: "",
                    collectionName = it.collectionName ?: "",
                    releaseDate = it.releaseDate ?: "",
                    primaryGenreName = it.primaryGenreName ?: "",
                    country = it.country ?: "",
                    previewUrl = it.previewUrl ?: ""
                )
            }

            val limitedTrackList = trackList.take(2) // два трека как образец для лога
            Timber.d("=== class TracksRepositoryImpl => return TracksResponse(${limitedTrackList})")
            return TracksResponse(trackList, response.resultCode)
        } else return TracksResponse(emptyList(), response.resultCode)
    }
}