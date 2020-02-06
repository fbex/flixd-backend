package io.fbex.flixd.backend.vod

import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.core.io.ClassPathResource
import java.time.LocalDate

internal class VodFacadeTests {

    private val imdbId = 680
    private val title = "Pulp Fiction"
    private val releaseDate: LocalDate = LocalDate.parse("1994-09-10")

    private val objectMapper = ObjectMapper().findAndRegisterModules()
    private val justWatchService: JustWatchService = mockk()
    private val testee = VodFacade(justWatchService)

    @Test
    fun `parses all matching data`() {
        val response = ClassPathResource("/vod/justwatch_search-pulp_fiction.json")
        val jsonResponse = objectMapper.readTree(response.file)

        coEvery { justWatchService.searchMovie(title, releaseDate.year) } returns jsonResponse

        val vodInformation = runBlocking { testee.searchMovie(imdbId, title, releaseDate) }

        assertThat(vodInformation).isEqualToComparingFieldByField(VOD_PULP_FICTION)
    }

    @Test
    fun `returns null if service returns an empty JSON node`() {
        val response = "{}".toJsonNode()

        coEvery { justWatchService.searchMovie(title, releaseDate.year) } returns response

        val vodInformation = runBlocking { testee.searchMovie(imdbId, title, releaseDate) }

        assertThat(vodInformation).isNull()
    }

    @Test
    fun `returns null if service returns an empty items list`() {
        val response = """
            {
                "page_size": 1,
                "items": []
            }
        """.trimIndent().toJsonNode()

        coEvery { justWatchService.searchMovie(title, releaseDate.year) } returns response

        val vodInformation = runBlocking { testee.searchMovie(imdbId, title, releaseDate) }

        assertThat(vodInformation).isNull()
    }

    @Test
    fun `returns the minimal result set if some data is missing in JustWatch response`() {
        val response = """
            {
              "page": 0,
              "page_size": 1,
              "total_pages": 1,
              "total_results": 1,
              "items": [
                {
                  "id": 112130,
                  "title": "Pulp Fiction",
                  "original_release_year": 1994,
                  "original_title": "Pulp Fiction"
                }
              ]
            }
        """.trimIndent().toJsonNode()
        val expectedVodInformation = VodInformation(
            justWatchId = 112130,
            title = "Pulp Fiction",
            year = 1994,
            tmdbId = null,
            tomatoId = null,
            offers = emptyList()
        )

        coEvery { justWatchService.searchMovie(title, releaseDate.year) } returns response

        val vodInformation = runBlocking { testee.searchMovie(imdbId, title, releaseDate) }

        assertThat(vodInformation).isEqualTo(expectedVodInformation)
    }

    @Test
    fun `returns null if the response data doesn't match with the requested parameters`() {
        val response = """
            {
              "page": 0,
              "page_size": 1,
              "total_pages": 1,
              "total_results": 1,
              "items": [
                {
                  "id": 20164,
                  "title": "The Room",
                  "original_release_year": 2003,
                  "original_title": "The Room"
                }
              ]
            }
        """.trimIndent().toJsonNode()

        coEvery { justWatchService.searchMovie(title, releaseDate.year) } returns response

        val vodInformation = runBlocking { testee.searchMovie(imdbId, title, releaseDate) }

        assertThat(vodInformation).isNull()
    }

    private fun String.toJsonNode() = objectMapper.readTree(this)
}
