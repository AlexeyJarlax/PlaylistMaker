package com.practicum.playlistmaker.garbage__data

// для ПлейАктивити

import com.practicum.playlistmaker.garbage__domain.api.RepositoryForSelectedTrack
import com.practicum.playlistmaker.garbage__domain.models.TracksList

class RepositoryImplForSelectedTrack : RepositoryForSelectedTrack {
    override suspend fun getTrack(url: String): TracksList {
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