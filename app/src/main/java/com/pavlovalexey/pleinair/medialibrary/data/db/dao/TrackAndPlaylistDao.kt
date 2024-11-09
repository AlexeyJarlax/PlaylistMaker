package com.practicum.playlistmaker.medialibrary.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

import com.practicum.playlistmaker.medialibrary.data.db.entity.TrackAndPlaylistEntity

@Dao
interface TrackAndPlaylistDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: TrackAndPlaylistEntity)

    @Query("SELECT * FROM track_in_playlist WHERE trackId IN (:tracksIds)")
    fun getAllTracksOfPlaylist(tracksIds: List<Int>): Flow<List<TrackAndPlaylistEntity>>

    @Query("SELECT * FROM track_in_playlist WHERE trackId = :playlistId")
    suspend fun getTrackById(playlistId: Int): TrackAndPlaylistEntity

    @Query("SELECT trackId FROM track_in_playlist")
    suspend fun getAllIdsTrack(): List<Int>

    @Query("DELETE FROM track_in_playlist WHERE trackId = :trackId")
    suspend fun deleteTrackFromPlaylistById(trackId: Int)
}