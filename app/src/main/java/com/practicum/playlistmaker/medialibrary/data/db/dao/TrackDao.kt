package com.practicum.playlistmaker.medialibrary.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

import com.practicum.playlistmaker.medialibrary.data.db.entity.TrackEntity

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Query("DELETE FROM track_table WHERE trackId = :trackId")
    suspend fun deleteTrack(trackId: Int)

    @Query("DELETE FROM track_table WHERE trackId = :trackId")
    suspend fun deleteTrackById(trackId: Int)

    @Query("SELECT * FROM track_table ORDER BY id DESC")
    fun getAllTrack(): Flow<List<TrackEntity>>

    @Query("SELECT trackId FROM track_table ")
    fun getIdsTracks(): Flow<List<Int>>

    @Query("SELECT * FROM track_table WHERE trackId = :trackId")
    suspend fun getTrackById(trackId: Int): TrackEntity?
}