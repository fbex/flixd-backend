package io.fbex.flixd.backend.media.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import java.time.LocalDate

@JsonInclude(NON_NULL)
data class BasicMediaItem(
    val mediaId: MediaId,
    val tmdbId: Int,
    val imdbId: String?,
    val type: MediaType,
    val title: String,
    val originalTitle: String,
    val originalLanguage: String,
    val releaseDate: LocalDate?,
    val voteAverage: Double,
    val voteCount: Int,
    val popularity: Double,
    val genreIds: List<Int>,
    val posterPath: String?,
    val backdropPath: String?
)

fun Movie.toBasicMediaItem() = BasicMediaItem(
    mediaId = mediaId,
    tmdbId = tmdbId,
    imdbId = imdbId,
    type = MediaType.Movie,
    title = title,
    originalTitle = originalTitle,
    originalLanguage = originalLanguage,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    voteCount = voteCount,
    popularity = popularity,
    genreIds = genres.map { it.id },
    posterPath = posterPath,
    backdropPath = backdropPath
)

fun TvShow.toBasicMediaItem() = BasicMediaItem(
    mediaId = mediaId,
    tmdbId = tmdbId,
    imdbId = imdbId,
    type = MediaType.TvShow,
    title = name,
    originalTitle = originalName,
    originalLanguage = originalLanguage,
    releaseDate = firstAirDate,
    voteAverage = voteAverage,
    voteCount = voteCount,
    popularity = popularity,
    genreIds = genres.map { it.id },
    posterPath = posterPath,
    backdropPath = backdropPath
)
