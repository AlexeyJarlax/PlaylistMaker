package com.practicum.playlistmaker.medialibrary.data.db

import com.practicum.playlistmaker.medialibrary.domain.db.Playlist
import com.practicum.playlistmaker.search.domain.models.Track

// Функция-расширение для преобразования объектов Track и TrackEntity друг в друга

    fun Track.toTrackEntity(): TrackEntity {
        return TrackEntity(
            id = null,
            trackId = this.trackId,
            trackName = this.trackName,
            artistName = this.artistName,
            trackTimeMillis = this.trackTimeMillis,
            artworkUrl100 = this.artworkUrl100,
            collectionName = this.collectionName,
            releaseDate = this.releaseDate,
            primaryGenreName = this.primaryGenreName,
            country = this.country,
            previewUrl = this.previewUrl
        )
    }

    fun TrackEntity.toTrack(): Track {
        return Track(
            trackId = this.trackId,
            trackName = this.trackName,
            artistName = this.artistName,
            trackTimeMillis = this.trackTimeMillis,
            artworkUrl100 = this.artworkUrl100,
            collectionName = this.collectionName,
            releaseDate = this.releaseDate,
            primaryGenreName = this.primaryGenreName,
            country = this.country,
            previewUrl = this.previewUrl
        )
    }

fun Playlist.toPlaylistEntity(): PlaylistEntity {
    return PlaylistEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        poster = this.poster,
        count = this.count
    )
}

fun PlaylistEntity.toPlaylist(): Playlist {
    return Playlist(
        id = this.id,
        title = this.title,
        description = this.description,
        poster = this.poster,
        count = this.count
    )
}

