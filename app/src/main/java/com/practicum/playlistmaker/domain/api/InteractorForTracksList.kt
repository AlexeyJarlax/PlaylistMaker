package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.TracksList

interface InteractorForTracksList {
    fun searchStep3useAPI(query: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTrack: List<TracksList>)
    }
}