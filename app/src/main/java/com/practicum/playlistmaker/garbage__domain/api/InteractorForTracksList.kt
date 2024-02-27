package com.practicum.playlistmaker.garbage__domain.api

import com.practicum.playlistmaker.garbage__domain.models.TracksList

interface InteractorForTracksList {
    fun searchStep3useAPI(query: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTrack: List<TracksList>)
    }
}