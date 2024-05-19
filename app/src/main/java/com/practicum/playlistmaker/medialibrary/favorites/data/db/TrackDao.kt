package com.practicum.playlistmaker.medialibrary.favorites.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.medialibrary.favorites.data.db.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao

interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Query("SELECT * FROM tracks_table")
    suspend fun getAllTracks(): List<TrackEntity>

    @Query("DELETE FROM tracks_table WHERE trackId = :trackId")
    suspend fun deleteTrack(trackId: String)

    @Query("SELECT * FROM tracks_table")
    fun getHistory(): Flow<List<Int>>
}