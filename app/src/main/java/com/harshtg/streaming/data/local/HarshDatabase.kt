package com.harshtg.streaming.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WatchProgressEntity::class], version = 1, exportSchema = false)
abstract class HarshDatabase : RoomDatabase() {
    abstract fun watchProgressDao(): WatchProgressDao
}
