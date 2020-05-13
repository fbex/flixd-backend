package io.fbex.flixd.backend.media.tmdb

import com.fasterxml.jackson.databind.JsonNode
import io.fbex.flixd.backend.media.model.Genre
import io.fbex.flixd.backend.media.model.Movie
import java.time.LocalDate

internal fun parseMovie(node: JsonNode): Movie {
    return Movie(
        title = node.get("title").asText(),
        originalTitle = node.get("original_title").asText(),
        releaseDate = node.get("release_date").asLocalDate(),
        tmdbId = node.get("id").asInt(),
        imdbId = node.get("imdb_id").asText(),
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
