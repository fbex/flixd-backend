package io.fbex.flixd.backend.tmdb

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.fasterxml.jackson.databind.JsonNode
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
    enum class Type(
        val tmdbName: String,
        val titleProperty: String,
        val originalTitleProperty: String,
        val releaseDateProperty: String
    ) {
        Movie(
            tmdbName = "movie",
            titleProperty = "title",
            originalTitleProperty = "original_title",
            releaseDateProperty = "release_date"
        ),
        TvShow(
            tmdbName = "tv",
            titleProperty = "name",
            originalTitleProperty = "original_name",
            releaseDateProperty = "first_air_date"
        )
    }
}

fun parseMediaSearchItem(node: JsonNode): MediaSearchItem {
    val type = node.get("media_type").asMediaItemType()
    return MediaSearchItem(
        tmdbId = node.get("id").asInt(),
        type = type,
        title = node.get(type.titleProperty).asText(),
        originalTitle = node.get(type.originalTitleProperty).asText(),
        originalLanguage = node.get("original_language").asText(),
        releaseDate = node.get(type.releaseDateProperty)?.asLocalDateOrNull(),
        voteAverage = node.get("vote_average").asDouble(),
        voteCount = node.get("vote_count").asInt(),
        popularity = node.get("popularity").asDouble(),
        genreIds = node.get("genre_ids").map { it.asInt() },
        posterPath = node.get("poster_path")?.asTextOrNull(),
        backdropPath = node.get("backdrop_path")?.asTextOrNull()
    )
}

private fun JsonNode.asLocalDateOrNull(): LocalDate? =
    asText().takeUnless { it.isBlank() || it == "null" }?.let { LocalDate.parse(it) }

private fun JsonNode.asTextOrNull(): String? =
    asText().takeUnless { it.isBlank() || it == "null" }

private fun JsonNode.asMediaItemType(): MediaSearchItem.Type = when (val value = asText()) {
    "movie" -> MediaSearchItem.Type.Movie
    "tv" -> MediaSearchItem.Type.TvShow
    else -> error("MediaItem.Type [$value] is not supported")
}
