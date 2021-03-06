package io.fbex.flixd.backend.media.tmdb

import com.fasterxml.jackson.databind.JsonNode
import io.fbex.flixd.backend.common.asLocalDate
import io.fbex.flixd.backend.common.asTextOrNull
import io.fbex.flixd.backend.media.model.Genre
import io.fbex.flixd.backend.media.model.MediaId
import io.fbex.flixd.backend.media.model.MediaType
import io.fbex.flixd.backend.media.model.Movie

internal fun parseMovie(node: JsonNode): Movie {
    val tmdbId = node.get("id").asInt()
    return Movie(
        title = node.get("title").asText(),
        originalTitle = node.get("original_title").asText(),
        releaseDate = node.get("release_date").asLocalDate(),
        mediaId = MediaId(tmdbId, MediaType.Movie),
        tmdbId = tmdbId,
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
