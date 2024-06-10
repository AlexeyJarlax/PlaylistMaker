package com.practicum.playlistmaker.medialibrary.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

import com.practicum.playlistmaker.medialibrary.data.db.entity.TrackAndPlaylistEntity

@Dao
interface TrackAndPlaylistDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: TrackAndPlaylistEntity)
}