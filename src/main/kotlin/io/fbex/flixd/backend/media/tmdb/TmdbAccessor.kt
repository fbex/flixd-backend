package io.fbex.flixd.backend.media.tmdb

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.fbex.flixd.backend.common.call
import io.fbex.flixd.backend.common.httpStatus
import io.fbex.flixd.backend.common.isNotFound
import io.fbex.flixd.backend.common.queryParameters
import io.fbex.flixd.backend.media.model.MediaSearchResult
import io.fbex.flixd.backend.media.model.Movie
import io.fbex.flixd.backend.media.model.TvShow
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

/**
 * Accessor for the TMDB API v3.
 *
 * For further documentation see:
 * https://developers.themoviedb.org/3/getting-started/introduction
 */
@Service
internal class TmdbAccessor(private val properties: TmdbProperties) {

    private val httpClient = OkHttpClient()
    private val objectMapper = jacksonObjectMapper().findAndRegisterModules()
    private val searchUrl = "${properties.url}/search/multi"
    private val movieUrl = "${properties.url}/movie"
    private val tvUrl = "${properties.url}/tv"
    private val relevantMediaTypes = MediaType.values().map { it.tmdbName }

    /**
     * Search for movies or tv shows in a single request.
     *
     * The [SearchResults][MediaSearchResult] are sorted
     * descending by popularity.
     *
     * For further documentation see:
     * https://developers.themoviedb.org/3/search/multi-search
     *
     * @return [MediaSearchResult]
     */
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

    /**
     * Get a [Movie] by its TMDB-ID.
     *
     * For further documentation see:
     * https://developers.themoviedb.org/3/movies/get-movie-details
     *
     * @return [Movie]
     */
    fun findMovie(tmdbId: Int): Movie? = doFind("$movieUrl/$tmdbId") { parseMovie(it) }

    /**
     * Get [TvShow] details by its TMDB-ID.
     *
     * For further documentation see:
     * https://developers.themoviedb.org/3/tv/get-tv-details
     *
     * @return [TvShow]
     */
    fun findTvShow(tmdbId: Int): TvShow? = doFind("$tvUrl/$tmdbId") { parseTvShow(it) }

    private fun <R> doFind(url: String, block: (JsonNode) -> R): R? {
        val response = httpClient.call(url) {
            get()
            queryParameters {
                addParameter("api_key", properties.apiKey)
                addParameter("language", properties.language)
            }
        }
        return when {
            response.isSuccessful -> parseBody(response.body, block)
            response.isNotFound -> null
            else -> throw TmdbResponseException(response.httpStatus)
        }
    }

    private fun <R> parseBody(body: ResponseBody?, block: (JsonNode) -> R): R? {
        if (body == null) error("Received no response body. Could not parse.")
        val jsonBody = objectMapper.readTree(body.byteStream())
        return block(jsonBody)
    }
}

class TmdbResponseException(httpStatus: HttpStatus) :
    IllegalStateException("TMDB responded with http status [$httpStatus]")
