package io.fbex.flixd.backend.tmdb

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.notFound
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.serverError
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

internal class TmdbServiceTests {

    private val apiKey = "1234"

    private lateinit var wireMock: WireMockServer
    private lateinit var testee: TmdbService

    @BeforeEach
    fun setUp() {
        wireMock = WireMockServer(options().dynamicPort())
        wireMock.start()
        val properties = TmdbProperties(
            url = "http://localhost:${wireMock.port()}",
            apiKey = apiKey
        )
        testee = TmdbService(properties)
    }

    @AfterEach
    fun tearDown() {
        wireMock.stop()
    }

    @DisplayName("Search movie")
    @Nested
    inner class SearchMovie {

        @Test
        fun `search movie responds with results`() {
            val response = ClassPathResource("/tmdb/movie_search-pulp_fiction.json")
            wireMock.givenThat(
                get(url("pulp fiction"))
                    .willReturn(
                        ok()
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                            .withBody(response.inputStream.readAllBytes())
                    )
            )

            val result = runBlocking { testee.search("pulp fiction") }

            with(result) {
                assertThat(page).isEqualTo(1)
                assertThat(total_pages).isEqualTo(1)
                assertThat(total_results).isEqualTo(5)
                assertThat(results).hasSize(5)
                assertThat(results[0]).isEqualToComparingFieldByField(MOVIE_PULP_FICTION_SEARCH)
            }
        }

        @Test
        fun `search movie responds with no results`() {
            val response = """
                {
                    "page": 1,
                    "total_results": 0,
                    "total_pages": 0,
                    "results": []
                }
            """.trimIndent()
            wireMock.givenThat(
                get(url("xxx"))
                    .willReturn(
                        ok()
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                            .withBody(response)
                    )
            )

            val result = runBlocking { testee.search("xxx") }

            with(result) {
                assertThat(page).isEqualTo(1)
                assertThat(total_pages).isEqualTo(0)
                assertThat(total_results).isEqualTo(0)
                assertThat(results).hasSize(0)
            }
        }

        @Test
        fun `throws for unsuccessful response`() {
            wireMock.givenThat(
                get(url("xxx"))
                    .willReturn(serverError())
            )

            assertThatThrownBy {
                runBlocking { testee.search("xxx") }
            }
                .isInstanceOf(TmdbResponseException::class.java)
                .hasMessage("TMDb responded with http status [500 INTERNAL_SERVER_ERROR]")
        }

        private fun url(query: String): String {
            val encoded = URLEncoder.encode(query, StandardCharsets.UTF_8.toString()).replace("+", "%20")
            return "/search/movie?api_key=$apiKey&language=en-US&page=1&include_adult=false&query=$encoded"
        }
    }

    @DisplayName("Get movie details")
    @Nested
    inner class GetMovieDetails {

        private val pulpFictionId = 680
        private val goodfellasId = 769
        private val theGodfatherId = 238

        @Test
        fun `get movie details for "Pulp Fiction"`() {
            val response = ClassPathResource("/tmdb/movie_details-pulp_fiction.json")
            wireMock.givenThat(
                get(url(pulpFictionId))
                    .willReturn(
                        ok()
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                            .withBody(response.inputStream.readAllBytes())
                    )
            )

            val result = runBlocking { testee.getMovieDetails(pulpFictionId) }

            assertThat(result).isEqualToComparingFieldByField(MOVIE_PULP_FICTION_DETAILS)
        }

        @Test
        fun `get movie details for "Goodfellas"`() {
            val response = ClassPathResource("/tmdb/movie_details-goodfellas.json")
            wireMock.givenThat(
                get(url(goodfellasId))
                    .willReturn(
                        ok()
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                            .withBody(response.inputStream.readAllBytes())
                    )
            )

            val result = runBlocking { testee.getMovieDetails(goodfellasId) }

            assertThat(result).isEqualToComparingFieldByField(MOVIE_GOODFELLAS_DETAILS)
        }

        @Test
        fun `get movie details for "The Godfather"`() {
            val response = ClassPathResource("/tmdb/movie_details-the_godfather.json")
            wireMock.givenThat(
                get(url(theGodfatherId))
                    .willReturn(
                        ok()
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                            .withBody(response.inputStream.readAllBytes())
                    )
            )

            val result = runBlocking { testee.getMovieDetails(theGodfatherId) }

            assertThat(result).isEqualToComparingFieldByField(MOVIE_THE_GODFATHER_DETAILS)
        }

        @Test
        fun `returns null if movie is not found`() {
            val response = """
                {
                    "status_code": 34,
                    "status_message": "The resource you requested could not be found."
                }
            """.trimIndent()
            wireMock.givenThat(
                get(url(1))
                    .willReturn(
                        notFound()
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                            .withBody(response)
                    )
            )

            val result = runBlocking { testee.getMovieDetails(1) }

            assertThat(result).isNull()
        }

        @Test
        fun `throws for unsuccessful response`() {
            wireMock.givenThat(
                get(url(1))
                    .willReturn(serverError())
            )

            assertThatThrownBy {
                runBlocking { testee.getMovieDetails(1) }
            }
                .isInstanceOf(TmdbResponseException::class.java)
                .hasMessage("TMDb responded with http status [500 INTERNAL_SERVER_ERROR]")
        }

        private fun url(tmdbId: Int) = "/movie/$tmdbId?api_key=$apiKey&language=en-US"
    }
}
