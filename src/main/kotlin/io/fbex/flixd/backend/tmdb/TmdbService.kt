package io.fbex.flixd.backend.tmdb

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.fbex.flixd.backend.common.call
import io.fbex.flixd.backend.common.httpStatus
import io.fbex.flixd.backend.common.queryParameters
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class TmdbService(private val properties: TmdbProperties) {

    private val httpClient = OkHttpClient()
    private val objectMapper = jacksonObjectMapper().findAndRegisterModules()
    private val searchUrl = "${properties.url}/search/multi"
    private val relevantMediaTypes: List<String> = MediaItem.Type.values().map { it.tmdbName }

    fun search(query: String): MediaSearchResult {
        val response = httpClient.call(searchUrl) {
            get()
            queryParameters {
                addParameter("api_key", properties.apiKey)
                addParameter("language", "de-DE")
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

    private fun parseSearchResults(response: ResponseBody?): MediaSearchResult {
        if (response == null) error("Received no response body. Could not parse.")
        val jsonBody = objectMapper.readTree(response.byteStream())
        val rawResults = jsonBody.get("results")?.toList() ?: emptyList()
        val results = rawResults
            .filter { it.get("media_type").asText() in relevantMediaTypes }
            .map { parseMediaResult(it) }
            .sortedByDescending { it.popularity }
        return MediaSearchResult(results)
    }
}

class TmdbResponseException(httpStatus: HttpStatus) :
    IllegalStateException("TMDb responded with http status [$httpStatus]")
