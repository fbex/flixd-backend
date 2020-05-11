package io.fbex.flixd.backend.media.model

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

internal class MediaIdTests {

    private val objectMapper = jacksonObjectMapper()

    @ParameterizedTest
    @CsvSource(
        "123, Movie, movie-123",
        "456, TvShow, tv-456"
    )
    fun `sets TmdbId and MediaType accordingly`(tmdbId: Int, mediaType: MediaType, mediaId: MediaId) {
        assertThat(mediaId.tmdbId).isEqualTo(tmdbId)
        assertThat(mediaId.mediaType).isEqualTo(mediaType)
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "movie-0", "movie-1", "movie-2", "movie-3", "movie-4",
            "movie-5", "movie-6", "movie-7", "movie-8", "movie-9",
            "tv-0", "tv-1", "tv-2", "tv-3", "tv-4",
            "tv-5", "tv-6", "tv-7", "tv-8", "tv-9",
            "movie-12", "movie-123", "movie-1234", "movie-12345", "movie-123456",
            "movie-1234567", "movie-12345678", "movie-123456789", "movie-123456780",
            "tv-12", "tv-123", "tv-1234", "tv-12345", "tv-123456",
            "tv-1234567", "tv-12345678", "tv-123456789", "tv-123456780"
        ]
    )
    fun `successfully creates MediaId from a string`(input: String) {
        val mediaId = MediaId(input)
        assertThat(mediaId.toString()).isEqualTo(input)
    }

    @ParameterizedTest
    @CsvSource(
        "1, Movie, movie-1",
        "12, Movie, movie-12",
        "123, Movie, movie-123",
        "1234, Movie, movie-1234",
        "12345, Movie, movie-12345",
        "123456, Movie, movie-123456",
        "1234567, Movie, movie-1234567",
        "12345678, Movie, movie-12345678",
        "123456789, Movie, movie-123456789",
        "1234567890, Movie, movie-1234567890",
        "2147483647, Movie, movie-2147483647",
        "1, TvShow, tv-1",
        "12, TvShow, tv-12",
        "123, TvShow, tv-123",
        "1234, TvShow, tv-1234",
        "12345, TvShow, tv-12345",
        "123456, TvShow, tv-123456",
        "1234567, TvShow, tv-1234567",
        "12345678, TvShow, tv-12345678",
        "123456789, TvShow, tv-123456789",
        "1234567890, TvShow, tv-1234567890",
        "2147483647, TvShow, tv-2147483647"
    )
    fun `successfully creates MediaId from TmdbId and MediaType`(tmdbId: Int, mediaType: MediaType, actualId: MediaId) {
        val mediaId = MediaId(tmdbId, mediaType)
        assertThat(mediaId).isEqualTo(actualId)
        assertThat(mediaId.tmdbId).isEqualTo(tmdbId)
        assertThat(mediaId.mediaType).isEqualTo(mediaType)
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "movie", "movie-", "movie--", "movie--1", "movie-a",
            "tv", "tv-", "tv--", "tv--1", "tv-a",
            "m", "m-", "m--", "m--1", "m-1", "m-1",
            "t", "t-", "t--", "t--1", "t-1", "t-1"
        ]
    )
    fun `throws for invalid MediaId`(input: String) {
        assertThatThrownBy { MediaId(input) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Invalid MediaId [$input]")
    }

    @ParameterizedTest
    @ValueSource(strings = ["movie-2147483648", "tv-2147483648"])
    fun `throws if MediaId is too large for an Intger`(input: String) {
        assertThatThrownBy { MediaId(input) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("MediaId [$input] is too large.")
    }

    @Test
    fun `toString() returns the correct value`() {
        val mediaId = MediaId("movie-123")
        assertThat(mediaId.toString()).isEqualTo("movie-123")
    }

    @Test
    fun `serializes to JSON correctly`() {
        val mediaId = MediaId("movie-123")
        val json = objectMapper.writeValueAsString(mediaId)

        assertThat(json).isEqualTo("\"movie-123\"")
    }

    @Test
    fun `deserializes from JSON correctly`() {
        val expectedMediaId = MediaId("tv-456")
        val json = "\"tv-456\""
        val mediaId = objectMapper.readValue(json, MediaId::class.java)

        assertThat(mediaId).isEqualTo(expectedMediaId)
    }
}
