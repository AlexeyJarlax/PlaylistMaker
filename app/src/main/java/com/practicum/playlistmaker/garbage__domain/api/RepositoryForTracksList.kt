package com.practicum.playlistmaker.garbage__domain.api

import com.practicum.playlistmaker.garbage__domain.models.TracksList

interface RepositoryForTracksList {
    fun searchTracksList(query: String): List<TracksList>
}