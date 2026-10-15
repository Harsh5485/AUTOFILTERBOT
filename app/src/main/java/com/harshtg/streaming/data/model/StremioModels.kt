package com.harshtg.streaming.data.model

import com.google.gson.annotations.SerializedName

// Stremio Addon Manifest
data class StremioManifest(
    val id: String,
    val version: String,
    val name: String,
    val description: String?,
    val resources: List<String>?,
    val types: List<String>?,
    val catalogs: List<StremioCatalog>?
)

data class StremioCatalog(
    val type: String,
    val id: String,
    val name: String
)

// Catalog Response
data class StremioCatalogResponse(
    val metas: List<StremioMetaItem>
)

data class StremioMetaResponse(
    val meta: StremioMetaItem
)

data class StremioMetaItem(
    val id: String,
    val type: String,
    val name: String,
    val poster: String?,
    val background: String?,
    val logo: String?,
    val description: String?,
    val releaseInfo: String?,
    val imdbRating: String?,
    val genres: List<String>?,
    val videos: List<StremioVideoItem>?
)

data class StremioVideoItem(
    val id: String,
    val title: String,
    val season: Int,
    val episode: Int,
    val released: String?,
    val thumbnail: String?,
    val overview: String?
)

// Stream Response
data class StremioStreamResponse(
    val streams: List<StremioStream>
)

data class StremioStream(
    val url: String?,
    val title: String?,
    val name: String?,
    val description: String?,
    val quality: String?,
    val behaviorHints: Map<String, Any>?
)

// Telegram-Stremio Server Dashboard Data Models
data class ServerDashboardResponse(
    val status: String,
    val pingMs: Long,
    val activeBotTokens: Int,
    val dbConnected: Boolean,
    val channels: List<TelegramChannel>?,
    val toolsStatus: ToolsStatus?
)

data class TelegramChannel(
    val channelId: String,
    val title: String,
    val type: String, // auth, anime, subtitle
    val totalFilesIndexed: Int
)

data class ToolsStatus(
    val deadLinksCount: Int,
    val duplicatesCount: Int,
    val failedScanFilesCount: Int
)
