package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.domain.models.TracksResponse

interface TracksSearchRepository {
    fun searchTracks(expression: String): TracksResponse
}