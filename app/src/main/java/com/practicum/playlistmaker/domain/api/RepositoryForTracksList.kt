package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.TracksList

interface RepositoryForTracksList {
    fun searchTracksList(query: String): List<TracksList>
}