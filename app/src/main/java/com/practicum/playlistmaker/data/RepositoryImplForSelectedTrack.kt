package com.practicum.playlistmaker.data

// для ПлейАктивити

import com.practicum.playlistmaker.domain.api.RepositoryForSelectedTrack
import com.practicum.playlistmaker.domain.models.TracksList
import timber.log.Timber

class RepositoryImplForSelectedTrack : RepositoryForSelectedTrack {
    override suspend fun getTrack(url: String): TracksList {
        Timber.d("=== class RepositoryImplForSelectedTrack => Трек url: $url")
        Timber.d("=== class RepositoryImplForSelectedTrack => TracksList: $TracksList")
        return TracksList(
            trackName = "",
            artistName = "",
            trackTimeMillis = 0,
            artworkUrl100 = "",
            collectionName = "",
            releaseDate = "",
            primaryGenreName = "",
            country = "",
            previewUrl = ""
        )
    }
}