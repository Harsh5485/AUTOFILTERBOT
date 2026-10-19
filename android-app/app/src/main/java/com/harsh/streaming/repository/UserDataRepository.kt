package com.harsh.streaming.repository

import com.harsh.streaming.database.WatchHistoryDao
import com.harsh.streaming.database.WatchHistoryEntity
import com.harsh.streaming.database.WatchLaterDao
import com.harsh.streaming.database.WatchLaterEntity
import kotlinx.coroutines.flow.Flow

class UserDataRepository(
    private val historyDao: WatchHistoryDao,
    private val watchLaterDao: WatchLaterDao
) {
    val allHistory: Flow<List<WatchHistoryEntity>> = historyDao.getAllHistory()
    val continueWatching: Flow<List<WatchHistoryEntity>> = historyDao.getContinueWatching()
    val watchLaterList: Flow<List<WatchLaterEntity>> = watchLaterDao.getWatchLaterList()

    fun isWatchLater(mediaId: Int): Flow<Boolean> = watchLaterDao.isWatchLater(mediaId)

    suspend fun savePlaybackPosition(
        mediaId: Int,
        title: String,
        posterPath: String?,
        mediaType: String,
        seasonNumber: Int = 1,
        episodeNumber: Int = 1,
        positionMs: Long,
        durationMs: Long
    ) {
        val entity = WatchHistoryEntity(
            mediaId = mediaId,
            title = title,
            posterPath = posterPath,
            mediaType = mediaType,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber,
            positionMs = positionMs,
            durationMs = durationMs,
            lastWatchedTimestamp = System.currentTimeMillis()
        )
        historyDao.insertOrUpdateHistory(entity)
    }

    suspend fun getHistoryItem(mediaId: Int): WatchHistoryEntity? {
        return historyDao.getHistoryItem(mediaId)
    }

    suspend fun toggleWatchLater(
        mediaId: Int,
        title: String,
        posterPath: String?,
        mediaType: String,
        voteAverage: Double,
        currentlySaved: Boolean
    ) {
        if (currentlySaved) {
            watchLaterDao.deleteWatchLater(mediaId)
        } else {
            watchLaterDao.insertWatchLater(
                WatchLaterEntity(
                    mediaId = mediaId,
                    title = title,
                    posterPath = posterPath,
                    mediaType = mediaType,
                    voteAverage = voteAverage
                )
            )
        }
    }

    suspend fun clearHistory() {
        historyDao.clearHistory()
    }
}
