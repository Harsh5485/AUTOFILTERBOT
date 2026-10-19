package com.harsh.streaming.model

import com.google.gson.annotations.SerializedName

enum class MediaType {
    MOVIE, TV
}

data class MediaItem(
    val id: Int,
    val title: String,
    val overview: String?,
    val posterPath: String?,
    val backdropPath: String?,
    val mediaType: MediaType,
    val voteAverage: Double,
    val releaseDate: String?,
    val genreIds: List<Int> = emptyList()
) {
    val fullPosterUrl: String
        get() = if (!posterPath.isNullOrEmpty()) "https://image.tmdb.org/t/p/w500$posterPath" else ""

    val fullBackdropUrl: String
        get() = if (!backdropPath.isNullOrEmpty()) "https://image.tmdb.org/t/p/w1280$backdropPath" else fullPosterUrl
}

data class MediaDetail(
    val id: Int,
    val title: String,
    val overview: String?,
    val posterPath: String?,
    val backdropPath: String?,
    val mediaType: MediaType,
    val voteAverage: Double,
    val releaseDate: String?,
    val runtime: Int?,
    val genres: List<Genre>,
    val seasons: List<SeasonInfo> = emptyList(),
    val numberOfSeasons: Int = 0,
    val numberOfEpisodes: Int = 0,
    val tagLine: String? = null
) {
    val fullPosterUrl: String
        get() = if (!posterPath.isNullOrEmpty()) "https://image.tmdb.org/t/p/w500$posterPath" else ""

    val fullBackdropUrl: String
        get() = if (!backdropPath.isNullOrEmpty()) "https://image.tmdb.org/t/p/w1280$backdropPath" else fullPosterUrl
}

data class Genre(
    val id: Int,
    val name: String
)

data class SeasonInfo(
    val id: Int,
    val seasonNumber: Int,
    val name: String,
    val episodeCount: Int,
    val posterPath: String?,
    val overview: String?
)

data class EpisodeInfo(
    val id: Int,
    val episodeNumber: Int,
    val seasonNumber: Int,
    val name: String,
    val overview: String?,
    val stillPath: String?,
    val airDate: String?,
    val voteAverage: Double
) {
    val fullStillUrl: String
        get() = if (!stillPath.isNullOrEmpty()) "https://image.tmdb.org/t/p/w500$stillPath" else ""
}

data class StreamProvider(
    val id: String,
    val name: String,
    val description: String,
    val isDirectStreamSupported: Boolean = false
) {
    fun getEmbedUrl(mediaType: MediaType, tmdbId: Int, season: Int = 1, episode: Int = 1): String {
        return when (id) {
            "vidsrc_icu" -> if (mediaType == MediaType.MOVIE) "https://vidsrc.icu/embed/movie/$tmdbId" else "https://vidsrc.icu/embed/tv/$tmdbId/$season/$episode"
            "vidsrc_me" -> if (mediaType == MediaType.MOVIE) "https://vidsrc.me/embed/movie?tmdb=$tmdbId" else "https://vidsrc.me/embed/tv?tmdb=$tmdbId&season=$season&episode=$episode"
            "vidsrc_cc" -> if (mediaType == MediaType.MOVIE) "https://vidsrc.cc/v2/embed/movie/$tmdbId" else "https://vidsrc.cc/v2/embed/tv/$tmdbId/$season/$episode"
            "vidsrc_xyz" -> if (mediaType == MediaType.MOVIE) "https://vidsrc.xyz/embed/movie/$tmdbId" else "https://vidsrc.xyz/embed/tv/$tmdbId/$season/$episode"
            "vidlink" -> if (mediaType == MediaType.MOVIE) "https://vidlink.pro/movie/$tmdbId" else "https://vidlink.pro/tv/$tmdbId/$season/$episode"
            "smashystream" -> if (mediaType == MediaType.MOVIE) "https://embed.smashystream.com/playere.php?tmdb=$tmdbId" else "https://embed.smashystream.com/playere.php?tmdb=$tmdbId&s=$season&e=$episode"
            else -> if (mediaType == MediaType.MOVIE) "https://vidsrc.icu/embed/movie/$tmdbId" else "https://vidsrc.icu/embed/tv/$tmdbId/$season/$episode"
        }
    }
}
