package com.practicum.playlistmaker.domain.api

// для ПлейАктивити

import com.practicum.playlistmaker.domain.models.TracksList

interface RepositoryForSelectedTrack {
    suspend fun getTrack(url: String): TracksList

}
