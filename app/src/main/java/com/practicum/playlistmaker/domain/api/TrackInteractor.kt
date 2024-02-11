package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface TrackInteractor {
    fun searchTrack(query: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTrack: List<Track>)
    }
}