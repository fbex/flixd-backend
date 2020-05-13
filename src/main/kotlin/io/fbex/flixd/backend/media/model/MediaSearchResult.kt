package io.fbex.flixd.backend.media.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import java.time.LocalDate

data class MediaSearchResult(
    val results: List<MediaSearchItem>
)

@JsonInclude(NON_NULL)
data class MediaSearchItem(
    val tmdbId: Int,
    val type: Type,
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
) {
    enum class Type {
        Movie, TvShow
    }
}
