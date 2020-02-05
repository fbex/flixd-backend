package io.fbex.flixd.backend.tmdb

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.awaitExchange

@Service
class TmdbService(
    properties: TmdbProperties
) {

    private val language = "en-US"
    private val apiKey = properties.apiKey
    private val client = WebClient.create(properties.url)

    suspend fun search(query: String): TmdbSearchResult {
        val response = client.get()
            .uri { builder ->
                builder
                    .path("/search/movie")
                    .queryParam("api_key", apiKey)
                    .queryParam("language", language)
                    .queryParam("page", 1)
                    .queryParam("include_adult", false)
                    .queryParam("query", query)
                    .build()
            }
            .accept(APPLICATION_JSON)
            .awaitExchange()
        return when (response.statusCode()) {
            OK -> response.awaitBody()
            else -> throw TmdbResponseException(response.statusCode())
        }
    }

    suspend fun getMovieDetails(tmdbId: Int): TmdbMovieDetails? {
        val response = client.get()
            .uri { builder ->
                builder
                    .path("/movie/$tmdbId")
                    .queryParam("api_key", apiKey)
                    .queryParam("language", language)
                    .build()
            }
            .accept(APPLICATION_JSON)
            .awaitExchange()
        return when (response.statusCode()) {
            OK -> response.awaitBody()
            NOT_FOUND -> null
            else -> throw TmdbResponseException(response.statusCode())
        }
    }
}

class TmdbResponseException(httpStatus: HttpStatus) :
    IllegalStateException("TMDb responded with http status [$httpStatus]")
