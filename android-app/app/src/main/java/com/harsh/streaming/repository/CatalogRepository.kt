package com.harsh.streaming.repository

import com.harsh.streaming.BuildConfig
import com.harsh.streaming.model.*
import com.harsh.streaming.network.TmdbApiService
import com.harsh.streaming.network.TmdbMediaDto

class CatalogRepository(private val apiService: TmdbApiService) {

    private val apiKey = BuildConfig.TMDB_API_KEY

    suspend fun getTrending(page: Int = 1): Result<List<MediaItem>> = runCatching {
        val response = apiService.getTrending(apiKey, page)
        response.results.map { it.toDomain() }
    }

    suspend fun getPopularMovies(page: Int = 1): Result<List<MediaItem>> = runCatching {
        val response = apiService.getPopularMovies(apiKey, page)
        response.results.map { it.toDomain(MediaType.MOVIE) }
    }

    suspend fun getPopularTvShows(page: Int = 1): Result<List<MediaItem>> = runCatching {
        val response = apiService.getPopularTvShows(apiKey, page)
        response.results.map { it.toDomain(MediaType.TV) }
    }

    suspend fun getHindiDubbedContent(page: Int = 1): Result<List<MediaItem>> = runCatching {
        val movies = apiService.discoverMovies(
            apiKey = apiKey,
            language = "hi",
            sortBy = "popularity.desc",
            page = page
        ).results.map { it.toDomain(MediaType.MOVIE) }

        val tvShows = apiService.discoverTvShows(
            apiKey = apiKey,
            language = "hi",
            sortBy = "popularity.desc",
            page = page
        ).results.map { it.toDomain(MediaType.TV) }

        (movies + tvShows).sortedByDescending { it.voteAverage }
    }

    suspend fun searchContent(query: String, page: Int = 1): Result<List<MediaItem>> = runCatching {
        val response = apiService.searchMulti(apiKey, query, page)
        response.results
            .filter { it.mediaTypeStr == "movie" || it.mediaTypeStr == "tv" }
            .map { it.toDomain() }
    }

    suspend fun getMovieDetails(id: Int): Result<MediaDetail> = runCatching {
        val dto = apiService.getMovieDetails(id, apiKey)
        MediaDetail(
            id = dto.id,
            title = dto.title,
            overview = dto.overview,
            posterPath = dto.posterPath,
            backdropPath = dto.backdropPath,
            mediaType = MediaType.MOVIE,
            voteAverage = dto.voteAverage ?: 0.0,
            releaseDate = dto.releaseDate,
            runtime = dto.runtime,
            genres = dto.genres?.map { Genre(it.id, it.name) } ?: emptyList(),
            tagLine = dto.tagline
        )
    }

    suspend fun getTvDetails(id: Int): Result<MediaDetail> = runCatching {
        val dto = apiService.getTvDetails(id, apiKey)
        MediaDetail(
            id = dto.id,
            title = dto.name,
            overview = dto.overview,
            posterPath = dto.posterPath,
            backdropPath = dto.backdropPath,
            mediaType = MediaType.TV,
            voteAverage = dto.voteAverage ?: 0.0,
            releaseDate = dto.firstAirDate,
            runtime = null,
            genres = dto.genres?.map { Genre(it.id, it.name) } ?: emptyList(),
            seasons = dto.seasons?.map {
                SeasonInfo(
                    id = it.id,
                    seasonNumber = it.seasonNumber,
                    name = it.name,
                    episodeCount = it.episodeCount,
                    posterPath = it.posterPath,
                    overview = it.overview
                )
            } ?: emptyList(),
            numberOfSeasons = dto.numberOfSeasons ?: 0,
            numberOfEpisodes = dto.numberOfEpisodes ?: 0,
            tagLine = dto.tagline
        )
    }

    suspend fun getSeasonEpisodes(tvId: Int, seasonNumber: Int): Result<List<EpisodeInfo>> = runCatching {
        val dto = apiService.getTvSeasonDetails(tvId, seasonNumber, apiKey)
        dto.episodes?.map {
            EpisodeInfo(
                id = it.id,
                episodeNumber = it.episodeNumber,
                seasonNumber = it.seasonNumber,
                name = it.name,
                overview = it.overview,
                stillPath = it.stillPath,
                airDate = it.airDate,
                voteAverage = it.voteAverage ?: 0.0
            )
        } ?: emptyList()
    }

    fun getAvailableProviders(): List<StreamProvider> = listOf(
        StreamProvider("vidsrc_icu", "Server 1 (Primary 4K Fast)", "Ultra-Smooth High Speed Server"),
        StreamProvider("vidsrc_me", "Server 2 (Pro Multi-Sub)", "Multiple Audio/Subtitles Server"),
        StreamProvider("vidsrc_cc", "Server 3 (Ultra HD)", "High Quality Backup Stream"),
        StreamProvider("vidsrc_xyz", "Server 4 (Backup 1)", "Fast Alternative Stream"),
        StreamProvider("vidlink", "Server 5 (Backup 2)", "Direct Server Link")
    )

    private fun TmdbMediaDto.toDomain(forcedType: MediaType? = null): MediaItem {
        val type = forcedType ?: when (mediaTypeStr) {
            "tv" -> MediaType.TV
            else -> MediaType.MOVIE
        }
        return MediaItem(
            id = id,
            title = title ?: name ?: "Untitled",
            overview = overview,
            posterPath = posterPath,
            backdropPath = backdropPath,
            mediaType = type,
            voteAverage = voteAverage ?: 0.0,
            releaseDate = releaseDate ?: firstAirDate,
            genreIds = genreIds ?: emptyList()
        )
    }
}
