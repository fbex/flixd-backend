package io.fbex.flixd.backend.vod

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class VodInformationTests {

    private val title = "Pulp Fiction"
    private val titleCase = "Pulp FICTION"
    private val year = 1994
    private val tmdbId = 1337

    private val testee = VodInformation(
        justWatchId = 1,
        title = title,
        year = year,
        tmdbId = tmdbId,
        tomatoId = 789,
        offers = emptyList()
    )

    private val testeeWithoutTmdbId = VodInformation(
        justWatchId = 1,
        title = title,
        year = year,
        tmdbId = null,
        tomatoId = 789,
        offers = emptyList()
    )

    @Test
    fun `returns true if the tmdbId matches`() {
        val matchesQuery = testee.matchesQuery(
            tmdbId = tmdbId,
            title = "",
            year = 0
        )
        assertThat(matchesQuery).isTrue()
    }

    @Test
    fun `returns false if the tmdbId doesn't match`() {
        val matchesQuery = testee.matchesQuery(
            tmdbId = 815,
            title = "",
            year = 0
        )

        assertThat(matchesQuery).isFalse()
    }

    @Test
    fun `returns true if title matches`() {
        val matchesQuery = testeeWithoutTmdbId.matchesQuery(
            tmdbId = 0,
            title = title,
            year = year
        )

        assertThat(matchesQuery).isTrue()
    }

    @Test
    fun `returns false if title and original title doesn't match`() {
        val matchesQuery = testeeWithoutTmdbId.matchesQuery(
            tmdbId = 0,
            title = "murks",
            year = 0
        )

        assertThat(matchesQuery).isFalse()
    }

    @Test
    fun `returns false if year doesn't match`() {
        val matchesQuery = testeeWithoutTmdbId.matchesQuery(
            tmdbId = 0,
            title = title,
            year = 2019
        )

        assertThat(matchesQuery).isFalse()
    }

    @Test
    fun `returns true if title and year matches`() {
        val matchesQuery = testeeWithoutTmdbId.matchesQuery(
            tmdbId = 0,
            title = title,
            year = year
        )

        assertThat(matchesQuery).isTrue()
    }

    @Test
    fun `returns true if everything matches`() {
        val matchesQuery = testee.matchesQuery(
            tmdbId = tmdbId,
            title = title,
            year = year
        )

        assertThat(matchesQuery).isTrue()
    }

    @Test
    fun `returns true if titles have different cases`() {
        val matchesQuery = testeeWithoutTmdbId.matchesQuery(
            tmdbId = 0,
            title = titleCase,
            year = year
        )

        assertThat(matchesQuery).isTrue()
    }
}
