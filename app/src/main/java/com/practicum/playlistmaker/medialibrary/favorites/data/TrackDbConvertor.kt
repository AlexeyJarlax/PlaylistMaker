package com.practicum.playlistmaker.medialibrary.favorites.data

import com.practicum.playlistmaker.medialibrary.favorites.data.db.TrackEntity
import com.practicum.playlistmaker.search.data.dto.TrackDTO
import com.practicum.playlistmaker.search.domain.models.Track

// Для того чтобы преобразовывать модели одного слоя в модели другого слоя, используются специальные классы-конвертеры

class TrackDbConvertor {

    fun map(track: TrackDTO): TrackEntity {
        return TrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }

    fun map(track: TrackEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
        )
    }
}