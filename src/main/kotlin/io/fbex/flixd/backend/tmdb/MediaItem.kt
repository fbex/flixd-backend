package io.fbex.flixd.backend.tmdb

import com.fasterxml.jackson.databind.JsonNode
import java.time.LocalDate

interface MediaItem

data class Movie(
    val tmdbId: Int,
    val title: String,
    val originalTitle: String,
    val originalLanguage: String,
    val releaseDate: LocalDate,
    val voteAverage: Double,
    val voteCount: Int,
    val popularity: Double,
    val genres: List<Genre>,
    val posterPath: String?,
    val backdropPath: String?
) : MediaItem

data class Genre(val id: Int, val name: String)

fun parseMovie(node: JsonNode): Movie {
    return Movie(
        tmdbId = node.get("id").asInt(),
        title = node.get("title").asText(),
        originalTitle = node.get("original_title").asText(),
        originalLanguage = node.get("original_language").asText(),
        releaseDate = node.get("release_date").asLocalDate(),
        voteAverage = node.get("vote_average").asDouble(),
        voteCount = node.get("vote_count").asInt(),
        popularity = node.get("popularity").asDouble(),
        genres = node.get("genres").map {
            Genre(
                id = it.get("id").asInt(),
                name = it.get("name").asText()
            )
        },
        posterPath = node.get("poster_path")?.asTextOrNull(),
        backdropPath = node.get("backdrop_path")?.asTextOrNull()
    )
}

private fun JsonNode.asLocalDate(): LocalDate = LocalDate.parse(asText())

private fun JsonNode.asTextOrNull(): String? =
    asText().takeUnless { it.isBlank() || it == "null" }
