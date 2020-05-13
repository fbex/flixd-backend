package io.fbex.flixd.backend.media.tmdb

import com.fasterxml.jackson.databind.JsonNode
import io.fbex.flixd.backend.media.model.Genre
import io.fbex.flixd.backend.media.model.TvShow
import java.time.LocalDate

internal fun parseTvShow(node: JsonNode): TvShow {
    return TvShow(
        name = node.get("name").asText(),
        originalName = node.get("original_name").asText(),
        firstAirDate = node.get("first_air_date").asLocalDate(),
        lastAirDate = node.get("last_air_date").asLocalDate(),
        tmdbId = node.get("id").asInt(),
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

private fun JsonNode.asLocalDate(): LocalDate = LocalDate.parse(asText())

private fun JsonNode.asTextOrNull(): String? =
    asText().takeUnless { it.isBlank() || it == "null" }
