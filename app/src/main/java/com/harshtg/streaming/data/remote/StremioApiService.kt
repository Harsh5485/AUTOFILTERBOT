package com.harshtg.streaming.data.remote

import com.harshtg.streaming.data.model.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface StremioApiService {

    @GET("manifest.json")
    suspend fun getManifest(): Response<StremioManifest>

    @GET("catalog/{type}/{id}.json")
    suspend fun getCatalog(
        @Path("type") type: String,
        @Path("id") id: String
    ): Response<StremioCatalogResponse>

    @GET("meta/{type}/{id}.json")
    suspend fun getMeta(
        @Path("type") type: String,
        @Path("id") id: String
    ): Response<StremioMetaResponse>

    @GET("stream/{type}/{id}.json")
    suspend fun getStreams(
        @Path("type") type: String,
        @Path("id") id: String
    ): Response<StremioStreamResponse>

    @GET("api/dashboard")
    suspend fun getServerDashboard(): Response<ServerDashboardResponse>

    // Dynamic URL testing endpoint for verifying Server Login Credentials
    @GET
    suspend fun testConnection(@Url url: String): Response<StremioManifest>
}
