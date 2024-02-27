package com.practicum.playlistmaker.garbage__domain.api

// для ПлейАктивити

import com.practicum.playlistmaker.garbage__domain.models.TracksList

interface RepositoryForSelectedTrack {
    suspend fun getTrack(url: String): TracksList

}
