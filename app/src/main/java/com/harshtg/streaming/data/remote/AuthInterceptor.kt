package com.harshtg.streaming.data.remote

import com.harshtg.streaming.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Credentials
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val preferencesRepository: UserPreferencesRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val credentials = runBlocking { preferencesRepository.serverCredentials.first() }

        var requestBuilder = originalRequest.newBuilder()
            .header("User-Agent", "HarshTG-Android/1.0.0 (Mobile & TV)")

        if (credentials.hostUrl.isNotBlank()) {
            val savedHostUrl = credentials.hostUrl.toHttpUrlOrNull()
            if (savedHostUrl != null) {
                val newUrl = originalRequest.url.newBuilder()
                    .scheme(savedHostUrl.scheme)
                    .host(savedHostUrl.host)
                    .port(savedHostUrl.port)
                    .build()
                requestBuilder.url(newUrl)
            }
        }

        if (credentials.username.isNotBlank() && credentials.password.isNotBlank()) {
            val authHeader = Credentials.basic(credentials.username, credentials.password)
            requestBuilder.header("Authorization", authHeader)
        }

        return chain.proceed(requestBuilder.build())
    }
}
