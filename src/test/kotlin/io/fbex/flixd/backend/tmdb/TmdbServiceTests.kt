package io.fbex.flixd.backend.tmdb

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import io.fbex.flixd.backend.tmdb.MediaItem.Type.Movie
import io.fbex.flixd.backend.tmdb.MediaItem.Type.TvShow
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
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

    @Test
    fun `search responds with movie results`() {
        val response = ClassPathResource("/tmdb/search-shaws.json")
        wireMock.givenThat(
            WireMock.get(url("shaws"))
                .willReturn(
                    WireMock.ok()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(response.inputStream.readAllBytes())
                )
        )

        val result = testee.search("shaws")

        with(result) {
            assertThat(results).hasSize(6)
            assertThat(results.filter { it.type == Movie }).hasSize(6)
            assertThat(results.map { it.popularity })
                .containsExactly(45.386, 2.734, 2.394, 1.924, 1.847, 0.699)
            assertThat(results.first()).isEqualToComparingFieldByField(MOVIE_SHAWSHANK_REDEMPTION)
        }
    }

    @Test
    fun `search responds with tv results`() {
        val response = ClassPathResource("/tmdb/search-scrubs.json")
        wireMock.givenThat(
            WireMock.get(url("scrubs"))
                .willReturn(
                    WireMock.ok()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(response.inputStream.readAllBytes())
                )
        )

        val result = testee.search("scrubs")

        with(result) {
            assertThat(results).hasSize(3)
            assertThat(results.filter { it.type == TvShow }).hasSize(3)
            assertThat(results.map { it.popularity }).containsExactly(42.175, 0.6, 0.6)
            assertThat(results.first()).isEqualToComparingFieldByField(TV_SCRUBS)
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
            WireMock.get(url("xxx"))
                .willReturn(
                    WireMock.ok()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(response)
                )
        )

        val result = testee.search("xxx")

        assertThat(result.results).hasSize(0)
    }

    @Test
    fun `throws for unsuccessful response`() {
        wireMock.givenThat(
            WireMock.get(url("xxx"))
                .willReturn(WireMock.serverError())
        )

        assertThatThrownBy { testee.search("xxx") }
            .isInstanceOf(TmdbResponseException::class.java)
            .hasMessage("TMDb responded with http status [500 INTERNAL_SERVER_ERROR]")
    }

    private fun url(query: String): String {
        val encoded = URLEncoder.encode(query, StandardCharsets.UTF_8.toString()).replace("+", "%20")
        return "/search/multi?api_key=$apiKey&language=de-DE&page=1&include_adult=false&query=$encoded"
    }
}
