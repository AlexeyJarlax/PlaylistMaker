package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface TrackRepository {
    fun searchTrack(query: String): List<Track>
}