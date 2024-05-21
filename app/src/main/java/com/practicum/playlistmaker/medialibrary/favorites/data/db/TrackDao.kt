package com.practicum.playlistmaker.medialibrary.favorites.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao

interface TrackDao {

    @Query("SELECT trackId FROM tracks_table")
    fun getTrackIds(): Flow<List<Int>>

    @Query("SELECT * FROM tracks_table WHERE trackId = :trackId")
    suspend fun getTrackById(trackId: Int): TrackEntity?

    @Query("SELECT * FROM tracks_table ORDER BY id DESC")
    fun getAllTracksSortedById(): Flow<List<TrackEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTrack(trackEntity: TrackEntity)

    @Query("DELETE FROM tracks_table WHERE trackId = :trackId")
    suspend fun deleteTrackById(trackId: Int)
}