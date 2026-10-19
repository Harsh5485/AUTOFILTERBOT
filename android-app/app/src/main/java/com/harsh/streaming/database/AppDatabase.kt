package com.harsh.streaming.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "watch_history")
data class WatchHistoryEntity(
    @PrimaryKey val mediaId: Int,
    val title: String,
    val posterPath: String?,
    val mediaType: String, // MOVIE or TV
    val seasonNumber: Int = 1,
    val episodeNumber: Int = 1,
    val positionMs: Long = 0,
    val durationMs: Long = 0,
    val lastWatchedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "watch_later")
data class WatchLaterEntity(
    @PrimaryKey val mediaId: Int,
    val title: String,
    val posterPath: String?,
    val mediaType: String,
    val voteAverage: Double,
    val addedTimestamp: Long = System.currentTimeMillis()
)

@Dao
interface WatchHistoryDao {
    @Query("SELECT * FROM watch_history ORDER BY lastWatchedTimestamp DESC")
    fun getAllHistory(): Flow<List<WatchHistoryEntity>>

    @Query("SELECT * FROM watch_history WHERE positionMs > 0 AND (durationMs = 0 OR positionMs < durationMs * 0.95) ORDER BY lastWatchedTimestamp DESC")
    fun getContinueWatching(): Flow<List<WatchHistoryEntity>>

    @Query("SELECT * FROM watch_history WHERE mediaId = :mediaId LIMIT 1")
    suspend fun getHistoryItem(mediaId: Int): WatchHistoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateHistory(entity: WatchHistoryEntity)

    @Query("DELETE FROM watch_history WHERE mediaId = :mediaId")
    suspend fun deleteHistory(mediaId: Int)

    @Query("DELETE FROM watch_history")
    suspend fun clearHistory()
}

@Dao
interface WatchLaterDao {
    @Query("SELECT * FROM watch_later ORDER BY addedTimestamp DESC")
    fun getWatchLaterList(): Flow<List<WatchLaterEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM watch_later WHERE mediaId = :mediaId)")
    fun isWatchLater(mediaId: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchLater(entity: WatchLaterEntity)

    @Query("DELETE FROM watch_later WHERE mediaId = :mediaId")
    suspend fun deleteWatchLater(mediaId: Int)
}

@Database(
    entities = [WatchHistoryEntity::class, WatchLaterEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun watchHistoryDao(): WatchHistoryDao
    abstract fun watchLaterDao(): WatchLaterDao
}
