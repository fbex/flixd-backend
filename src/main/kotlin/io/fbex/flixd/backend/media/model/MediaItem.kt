package io.fbex.flixd.backend.media.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import java.time.LocalDate

sealed class MediaItem {
    abstract val mediaId: MediaId
    abstract val tmdbId: Int
    abstract val imdbId: String?
    abstract val voteAverage: Double
    abstract val voteCount: Int
    abstract val popularity: Double
    abstract val genres: List<Genre>
    abstract val originalLanguage: String
    abstract val posterPath: String?
    abstract val backdropPath: String?
}

@JsonInclude(NON_NULL)
data class Movie(
    val title: String,
    val originalTitle: String,
    val releaseDate: LocalDate,
    override val mediaId: MediaId,
    override val tmdbId: Int,
    override val imdbId: String?,
    override val voteAverage: Double,
    override val voteCount: Int,
    override val popularity: Double,
    override val genres: List<Genre>,
    override val originalLanguage: String,
    override val posterPath: String?,
    override val backdropPath: String?
) : MediaItem()

@JsonInclude(NON_NULL)
data class TvShow(
    val name: String,
    val originalName: String,
    val firstAirDate: LocalDate,
    val lastAirDate: LocalDate,
    override val mediaId: MediaId,
    override val tmdbId: Int,
    override val imdbId: String?,
    override val voteAverage: Double,
    override val voteCount: Int,
    override val popularity: Double,
    override val genres: List<Genre>,
    override val originalLanguage: String,
    override val posterPath: String?,
    override val backdropPath: String?
) : MediaItem()

data class Genre(val id: Int, val name: String)
