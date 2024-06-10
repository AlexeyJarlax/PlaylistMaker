package com.practicum.playlistmaker.medialibrary.data.db.converters

import com.practicum.playlistmaker.medialibrary.data.db.entity.TrackEntity
import com.practicum.playlistmaker.search.domain.models.Track

// Функция-расширение для преобразования объектов Track и TrackEntity друг в друга

class TrackDbConverter {

    fun trackToTrackEntity(track: Track): TrackEntity {
        return TrackEntity(
            0,
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.releaseDate,
            track.primaryGenreName,
            track.collectionName,
            track.country,
            track.previewUrl
        )
    }

    fun trackEntityToTrack(track: TrackEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.releaseDate,
            track.primaryGenreName,
            track.collectionName,
            track.country,
            track.previewUrl
        )
    }
}