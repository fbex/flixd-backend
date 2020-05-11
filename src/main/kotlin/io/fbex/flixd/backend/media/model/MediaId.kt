package io.fbex.flixd.backend.media.model

import com.fasterxml.jackson.annotation.JsonValue
import java.lang.NumberFormatException

/**
 * Abstraction for the external media id (TMDB).
 * Inherits the [MediaType] and the TmdbId.
 *
 * Example:
 *  - "movie-123"
 *  - "tv-456"
 */
data class MediaId(@JsonValue private val value: String) {

    private val idPattern = "^(movie|tv)-[0-9]{1,10}$".toRegex()

    val tmdbId: Int
    val mediaType: MediaType

    constructor(tmdbId: Int, mediaType: MediaType) :
            this("${mediaType.code}-$tmdbId")

    init {
        require(value.matches(idPattern)) { "Invalid MediaId [$value]" }
        val split = value.split("-")
        tmdbId = extractTmdbId(split[1])
        mediaType = MediaType.fromCode(split[0])
    }

    private fun extractTmdbId(value: String): Int = try {
        value.toInt()
    } catch (e: NumberFormatException) {
        throw IllegalArgumentException("MediaId [$this] is too large.")
    }

    override fun toString(): String = value
}
