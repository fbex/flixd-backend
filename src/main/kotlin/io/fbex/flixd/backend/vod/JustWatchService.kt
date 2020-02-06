package io.fbex.flixd.backend.vod

import com.fasterxml.jackson.databind.JsonNode
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters.fromValue
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.awaitExchange

@Service
class JustWatchService(
    properties: JustWatchProperties
) {

    private val language = "en_US"
    private val client = WebClient.create(properties.url)

    suspend fun searchMovie(title: String, year: Int): JsonNode {
        val request = JustWatchSearchRequest(
            query = title,
            release_year_from = year,
            release_year_until = year,
            content_types = listOf(JustWatchContentType.MOVIE),
            page_size = 1
        )
        val response = client.post()
            .uri("/content/titles/$language/popular")
            .accept(APPLICATION_JSON)
            .body(fromValue(request))
            .awaitExchange()
        return when (response.statusCode()) {
            OK -> response.awaitBody()
            else -> throw JustWatchResponseException(response.statusCode())
        }
    }
}

class JustWatchResponseException(httpStatus: HttpStatus) :
    IllegalStateException("JustWatch responded with http status [$httpStatus]")
