package io.fbex.flixd.backend.media.tmdb

import com.fasterxml.jackson.databind.JsonNode
import io.fbex.flixd.backend.common.asLocalDate
import io.fbex.flixd.backend.common.asTextOrNull
import io.fbex.flixd.backend.media.model.Genre
import io.fbex.flixd.backend.media.model.MediaId
import io.fbex.flixd.backend.media.model.MediaType
import io.fbex.flixd.backend.media.model.TvShow

internal fun parseTvShow(node: JsonNode): TvShow {
    val tmdbId = node.get("id").asInt()
    return TvShow(
        name = node.get("name").asText(),
        originalName = node.get("original_name").asText(),
        firstAirDate = node.get("first_air_date").asLocalDate(),
        lastAirDate = node.get("last_air_date").asLocalDate(),
        mediaId = MediaId(tmdbId, MediaType.TvShow),
        tmdbId = tmdbId,
        imdbId = node.get("imdb_id")?.asTextOrNull(),
        voteAverage = node.get("vote_average").asDouble(),
        voteCount = node.get("vote_count").asInt(),
        popularity = node.get("popularity").asDouble(),
        genres = node.get("genres").map {
            Genre(
                id = it.get("id").asInt(),
                name = it.get("name").asText()
            )
        },
        originalLanguage = node.get("original_language").asText(),
        posterPath = node.get("poster_path")?.asTextOrNull(),
        backdropPath = node.get("backdrop_path")?.asTextOrNull()
    )
}
