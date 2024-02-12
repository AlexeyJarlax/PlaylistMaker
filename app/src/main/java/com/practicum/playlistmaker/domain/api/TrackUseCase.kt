package com.practicum.playlistmaker.domain.api

// для ПлейАктивити

import com.practicum.playlistmaker.domain.models.TracksList

interface TrackUseCase {
    suspend fun getTrack(url: String): TracksList
}