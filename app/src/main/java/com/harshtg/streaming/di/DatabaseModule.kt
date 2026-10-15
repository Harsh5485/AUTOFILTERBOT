package com.harshtg.streaming.di

import android.content.Context
import androidx.room.Room
import com.harshtg.streaming.data.local.HarshDatabase
import com.harshtg.streaming.data.local.WatchProgressDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideHarshDatabase(@ApplicationContext context: Context): HarshDatabase {
        return Room.databaseBuilder(
            context,
            HarshDatabase::class.java,
            "harsh_tg.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideWatchProgressDao(database: HarshDatabase): WatchProgressDao {
        return database.watchProgressDao()
    }
}
