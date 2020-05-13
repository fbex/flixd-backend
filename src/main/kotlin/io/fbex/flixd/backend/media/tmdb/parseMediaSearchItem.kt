package io.fbex.flixd.backend.media.tmdb

import com.fasterxml.jackson.databind.JsonNode
import io.fbex.flixd.backend.media.model.MediaSearchItem
import java.time.LocalDate

internal fun parseMediaSearchItem(node: JsonNode): MediaSearchItem {
    val type = node.get("media_type").asMediaType()
    return MediaSearchItem(
        tmdbId = node.get("id").asInt(),
        type = type.mediaSearchItemType,
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

internal enum class MediaType(
    val mediaSearchItemType: MediaSearchItem.Type,
    val tmdbName: String,
    val titleProperty: String,
    val originalTitleProperty: String,
    val releaseDateProperty: String
) {
    Movie(
        mediaSearchItemType = MediaSearchItem.Type.Movie,
        tmdbName = "movie",
        titleProperty = "title",
        originalTitleProperty = "original_title",
        releaseDateProperty = "release_date"
    ),
    TvShow(
        mediaSearchItemType = MediaSearchItem.Type.TvShow,
        tmdbName = "tv",
        titleProperty = "name",
        originalTitleProperty = "original_name",
        releaseDateProperty = "first_air_date"
    )
}

private fun JsonNode.asLocalDateOrNull(): LocalDate? =
    asText().takeUnless { it.isBlank() || it == "null" }?.let { LocalDate.parse(it) }

private fun JsonNode.asTextOrNull(): String? =
    asText().takeUnless { it.isBlank() || it == "null" }

private fun JsonNode.asMediaType(): MediaType = when (val value = asText()) {
    "movie" -> MediaType.Movie
    "tv" -> MediaType.TvShow
    else -> error("MediaItem.Type [$value] is not supported")
}
