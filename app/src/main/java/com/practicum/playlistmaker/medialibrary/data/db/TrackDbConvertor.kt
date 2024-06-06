package com.practicum.playlistmaker.medialibrary.data.db

import com.practicum.playlistmaker.medialibrary.domain.db.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import android.net.Uri

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

fun Track.toSelectedTrackEntity(): SelectedTrackEntity? {
    return this.artworkUrl100?.let {
        SelectedTrackEntity(
            id = null,
            trackId = this.trackId ?: -1, // Provide a default value for trackId if it's nullable
            trackName = this.trackName ?: "Unknown Track", // Provide a default value for trackName if it's nullable
            artistName = this.artistName ?: "Unknown Artist", // Provide a default value for artistName if it's nullable
            trackTimeMillis = this.trackTimeMillis ?: 0L, // Provide a default value for trackTimeMillis if it's nullable
            artworkUrl100 = it,
            collectionName = this.collectionName ?: "Unknown Collection", // Provide a default value for collectionName if it's nullable
            releaseDate = this.releaseDate ?: "Unknown Date", // Provide a default value for releaseDate if it's nullable
            primaryGenreName = this.primaryGenreName ?: "Unknown Genre", // Provide a default value for primaryGenreName if it's nullable
            country = this.country ?: "Unknown Country", // Provide a default value for country if it's nullable
            previewUrl = this.previewUrl ?: "" // Provide a default value for previewUrl if it's nullable
        )
    }
}

fun SelectedTrackEntity.toTrack(): Track {
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

fun Playlist.toPlaylistEntity(filename: String): PlaylistEntity {
    return PlaylistEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        poster = filename,
        tracks = this.tracks,
        count = this.count
    )
}

fun PlaylistEntity.toPlaylist(uri: Uri?): Playlist {
    return Playlist(
        id = this.id,
        title = this.title,
        description = this.description,
        poster = uri,
        tracks = this.tracks,
        count = this.count
    )
}
