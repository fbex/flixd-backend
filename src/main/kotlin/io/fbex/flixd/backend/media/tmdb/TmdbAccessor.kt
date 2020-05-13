package io.fbex.flixd.backend.media.tmdb

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.fbex.flixd.backend.common.call
import io.fbex.flixd.backend.common.httpStatus
import io.fbex.flixd.backend.common.isNotFound
import io.fbex.flixd.backend.common.queryParameters
import io.fbex.flixd.backend.media.model.MediaSearchResult
import io.fbex.flixd.backend.media.model.Movie
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
internal class TmdbAccessor(private val properties: TmdbProperties) {

    private val httpClient = OkHttpClient()
    private val objectMapper = jacksonObjectMapper().findAndRegisterModules()
    private val searchUrl = "${properties.url}/search/multi"
    private val movieUrl = "${properties.url}/movie"
    private val relevantMediaTypes: List<String> = MediaType.values().map { it.tmdbName }

    fun search(query: String): MediaSearchResult {
        val response = httpClient.call(searchUrl) {
            get()
            queryParameters {
                addParameter("api_key", properties.apiKey)
                addParameter("language", properties.language)
                addParameter("page", "1")
                addParameter("include_adult", "false")
                addParameter("query", query)
            }
        }
        return when {
            response.isSuccessful -> parseSearchResults(response.body)
            else -> throw TmdbResponseException(response.httpStatus)
        }
    }

    private fun parseSearchResults(body: ResponseBody?): MediaSearchResult {
        if (body == null) error("Received no response body. Could not parse.")
        val jsonBody = objectMapper.readTree(body.byteStream())
        val rawResults = jsonBody.get("results")?.toList() ?: emptyList()
        val results = rawResults
            .filter { it.get("media_type").asText() in relevantMediaTypes }
            .map { parseMediaSearchItem(it) }
            .sortedByDescending { it.popularity }
        return MediaSearchResult(results)
    }

    fun findMovie(tmdbId: Int): Movie? {
        val response = httpClient.call("$movieUrl/$tmdbId") {
            get()
            queryParameters {
                addParameter("api_key", properties.apiKey)
                addParameter("language", properties.language)
            }
        }
        return when {
            response.isSuccessful -> parseMovie(response.body)
            response.isNotFound -> null
            else -> throw TmdbResponseException(response.httpStatus)
        }
    }

    private fun parseMovie(body: ResponseBody?): Movie {
        val jsonBody = objectMapper.readTree(body!!.byteStream())
        return parseMovie(jsonBody)
    }
}

class TmdbResponseException(httpStatus: HttpStatus) :
    IllegalStateException("TMDb responded with http status [$httpStatus]")
