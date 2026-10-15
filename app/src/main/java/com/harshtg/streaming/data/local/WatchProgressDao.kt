package com.harshtg.streaming.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchProgressDao {

    @Query("SELECT * FROM watch_progress ORDER BY lastUpdated DESC")
    fun getAllWatchProgress(): Flow<List<WatchProgressEntity>>

    @Query("SELECT * FROM watch_progress WHERE mediaId = :mediaId LIMIT 1")
    suspend fun getWatchProgress(mediaId: String): WatchProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveWatchProgress(progress: WatchProgressEntity)

    @Query("DELETE FROM watch_progress WHERE mediaId = :mediaId")
    suspend fun deleteWatchProgress(mediaId: String)
}
