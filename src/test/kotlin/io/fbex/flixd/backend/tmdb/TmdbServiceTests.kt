package io.fbex.flixd.backend.tmdb

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
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
    fun `search movie responds with results`() {
        val response = ClassPathResource("/tmdb/search-schweigen_der_l.json")
        wireMock.givenThat(
            WireMock.get(url("schweigen der l"))
                .willReturn(
                    WireMock.ok()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(response.inputStream.readAllBytes())
                )
        )

        val result = testee.searchMovie("schweigen der l")

        with(result) {
            assertThat(page).isEqualTo(1)
            assertThat(total_pages).isEqualTo(1)
            assertThat(total_results).isEqualTo(7)
            assertThat(results).hasSize(7)
            assertThat(results.last()).isEqualToComparingFieldByField(MOVIE_SILENCE_OF_THE_LAMBS)
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

        val result = testee.searchMovie("xxx")

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
            WireMock.get(url("xxx"))
                .willReturn(WireMock.serverError())
        )

        assertThatThrownBy { testee.searchMovie("xxx") }
            .isInstanceOf(TmdbResponseException::class.java)
            .hasMessage("TMDb responded with http status [500 INTERNAL_SERVER_ERROR]")
    }

    private fun url(query: String): String {
        val encoded = URLEncoder.encode(query, StandardCharsets.UTF_8.toString()).replace("+", "%20")
        return "/search/movie?api_key=$apiKey&language=de-DE&page=1&include_adult=false&query=$encoded"
    }
}
