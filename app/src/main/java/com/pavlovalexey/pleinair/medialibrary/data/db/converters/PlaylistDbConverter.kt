package com.practicum.playlistmaker.medialibrary.data.db.converters

import androidx.core.net.toUri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import com.practicum.playlistmaker.medialibrary.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.medialibrary.data.db.entity.TrackAndPlaylistEntity
import com.practicum.playlistmaker.medialibrary.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.models.Track

// Функция-расширение для преобразования объектов Track и TrackEntity друг в друга

class PlaylistDbConverter(private val gson: Gson) {

    fun playlistToPlaylistEntity(playlist: Playlist): PlaylistEntity {
        val listTrackId: String = gson.toJson(playlist.tracksIds, ArrayList<Int>()::class.java)
        return PlaylistEntity(
            playlist.id ?: 0,
            playlist.playlistName ?: "",
            playlist.description ?: "",
            playlist.urlImage?.toString() ?: "",
            listTrackId,
            playlist.tracksCount ?: 0
        )
    }

    fun playlistEntityToPlaylist(playlistEntity: PlaylistEntity): Playlist {
        val type = object : TypeToken<ArrayList<Int>>() {}.type
        val trackIds = gson.fromJson<ArrayList<Int>>(playlistEntity.tracksIds, type)
        return Playlist(
            playlistEntity.id,
            playlistEntity.playlistName,
            playlistEntity.description,
            if (playlistEntity.urlImage=="null") null else playlistEntity.urlImage.toUri(),
            trackIds ?: ArrayList(),
            playlistEntity.tracksCount
        )
    }

    fun trackToTrackPlaylistEntity(track: Track): TrackAndPlaylistEntity {
        return TrackAndPlaylistEntity(
            track.trackId ?: 0,
            track.trackName ?: "",
            track.artistName ?: "",
            track.trackTimeMillis ?: 0L,
            track.artworkUrl100 ?: "",
            track.releaseDate ?: "",
            track.primaryGenreName ?: "",
            track.collectionName ?: "",
            track.country ?: "",
            track.previewUrl ?: ""
        )
    }

    fun idsStringToList(string: String): List<Int>{
        val type = object : TypeToken<ArrayList<Int>>() {}.type
        val trackIds = gson.fromJson<ArrayList<Int>>(string, type)
        return trackIds
    }

    fun trackPlaylistEntityToTrack(trackAndPlaylistEntity: TrackAndPlaylistEntity): Track {
        return Track(
            trackAndPlaylistEntity.trackId,
            trackAndPlaylistEntity.trackName,
            trackAndPlaylistEntity.artistName,
            trackAndPlaylistEntity.trackTimeMillis,
            trackAndPlaylistEntity.artworkUrl100,
            trackAndPlaylistEntity.releaseDate,
            trackAndPlaylistEntity.primaryGenreName,
            trackAndPlaylistEntity.collectionName,
            trackAndPlaylistEntity.country,
            trackAndPlaylistEntity.previewUrl
        )
    }
}