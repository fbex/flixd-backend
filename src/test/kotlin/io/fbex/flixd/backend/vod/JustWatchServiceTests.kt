package io.fbex.flixd.backend.vod

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.okJson
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.serverError
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.core.io.ClassPathResource

internal class JustWatchServiceTests {

    private val url = "/content/titles/en_US/popular"
    private val objectMapper = ObjectMapper().findAndRegisterModules()

    private lateinit var wireMock: WireMockServer
    private lateinit var testee: JustWatchService

    @BeforeEach
    fun setUp() {
        wireMock = WireMockServer(options().dynamicPort())
        wireMock.start()
        val properties = JustWatchProperties(url = "http://localhost:${wireMock.port()}")
        testee = JustWatchService(properties)
    }

    @AfterEach
    fun tearDown() {
        wireMock.stop()
    }

    @Test
    fun `search title returns a JsonNode`() {
        val justWatchResponse = ClassPathResource("/vod/justwatch_search-pulp_fiction.json")
        val jsonResponse = justWatchResponse.inputStream.bufferedReader().readText()
        val requestBody = """
            {
                "query": "pulp fiction",
                "release_year_from": 1994,
                "release_year_until": 1994,
                "content_types": ["movie"],
                "page_size": 1
            }
        """.trimIndent()

        wireMock.givenThat(
            post(url)
                .withRequestBody(equalToJson(requestBody))
                .willReturn(okJson(jsonResponse))
        )

        val result = runBlocking { testee.searchMovie("pulp fiction", 1994) }

        assertThat(objectMapper.readTree(jsonResponse)).isEqualTo(result)
    }

    @Test
    fun `throws if backend response is not OK `() {
        val requestBody = """
            {
                "query": "matrix",
                "release_year_from": 1800,
                "release_year_until": 1800,
                "content_types": ["movie"],
                "page_size": 1
            }
        """.trimIndent()

        wireMock.givenThat(
            post(url)
                .withRequestBody(equalToJson(requestBody))
                .willReturn(serverError())
        )

        assertThatThrownBy {
            runBlocking { testee.searchMovie("matrix", 1800) }
        }
            .isInstanceOf(JustWatchResponseException::class.java)
            .hasMessage("JustWatch responded with http status [500 INTERNAL_SERVER_ERROR]")
    }
}
