package com.practicum.playlistmaker.medialibrary.favorites.data

import com.practicum.playlistmaker.medialibrary.favorites.data.TrackDao
import com.practicum.playlistmaker.medialibrary.favorites.data.TrackEntity

class TrackRepository(private val trackDao: TrackDao) {

    suspend fun addTrack(track: TrackEntity) {
        trackDao.insertTrack(track)
    }

    suspend fun getAllTracks(): List<TrackEntity> {
        return trackDao.getAllTracks()
    }

    suspend fun deleteTrack(trackId: String) {
        trackDao.deleteTrack(trackId)
    }
}