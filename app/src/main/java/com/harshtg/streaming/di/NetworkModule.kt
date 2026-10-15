package com.harshtg.streaming.di

import com.harshtg.streaming.data.remote.AuthInterceptor
import com.harshtg.streaming.data.remote.StremioApiService
import com.harshtg.streaming.data.repository.UserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideStremioApiService(
        okHttpClient: OkHttpClient,
        userPreferencesRepository: UserPreferencesRepository
    ): StremioApiService {
        val credentials = runBlocking { userPreferencesRepository.serverCredentials.first() }
        val baseUrl = if (credentials.hostUrl.isNotBlank()) {
            if (credentials.hostUrl.endsWith("/")) credentials.hostUrl else "${credentials.hostUrl}/"
        } else {
            "http://localhost:7000/"
        }

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StremioApiService::class.java)
    }
}
