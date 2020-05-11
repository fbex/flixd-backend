package io.fbex.flixd.backend.media.tmdb

import com.fasterxml.jackson.databind.JsonNode
import io.fbex.flixd.backend.common.asLocalDateOrNull
import io.fbex.flixd.backend.common.asTextOrNull
import io.fbex.flixd.backend.media.model.BasicMediaItem
import io.fbex.flixd.backend.media.model.MediaId
import io.fbex.flixd.backend.media.model.MediaType

/**
 * Parser for /search/multi results into [BasicMediaItem].
 */
internal fun parseBasicMediaItem(node: JsonNode): BasicMediaItem {
    val type = node.get("media_type").asMediaType()
    val tmdbId = node.get("id").asInt()
    return BasicMediaItem(
        mediaId = MediaId(tmdbId, type.mediaType),
        tmdbId = tmdbId,
        imdbId = null,
        type = type.mediaType,
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

internal enum class TmdbMediaType(
    val mediaType: MediaType,
    val tmdbName: String,
    val titleProperty: String,
    val originalTitleProperty: String,
    val releaseDateProperty: String
) {
    Movie(
        mediaType = MediaType.Movie,
        tmdbName = "movie",
        titleProperty = "title",
        originalTitleProperty = "original_title",
        releaseDateProperty = "release_date"
    ),
    Tv(
        mediaType = MediaType.TvShow,
        tmdbName = "tv",
        titleProperty = "name",
        originalTitleProperty = "original_name",
        releaseDateProperty = "first_air_date"
    )
}

private fun JsonNode.asMediaType(): TmdbMediaType = when (val value = asText()) {
    "movie" -> TmdbMediaType.Movie
    "tv" -> TmdbMediaType.Tv
    else -> error("MediaItem.Type [$value] is not supported")
}
