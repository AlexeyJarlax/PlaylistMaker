package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.dto.SearchRequest
import com.practicum.playlistmaker.search.data.dto.SearchResponse
import com.practicum.playlistmaker.search.domain.TracksSearchRepository
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.models.TracksResponse
import timber.log.Timber

class RepositoryImplForTracksList(private val networkClient: NetworkClient): TracksSearchRepository {

    override fun searchTracks(expression: String): TracksResponse {
        val response = networkClient.doRequest(SearchRequest(expression))
        if (response.resultCode == 200) {
            val trackList = (response as SearchResponse).results.map {
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
            Timber.d("=== class TracksRepositoryImpl => return TracksResponse(${trackList})")
            return TracksResponse(trackList, response.resultCode)
        } else return TracksResponse(emptyList(), response.resultCode)
    }
}