package io.fbex.flixd.backend.tmdb

import io.fbex.flixd.backend.common.call
import io.fbex.flixd.backend.common.httpStatus
import io.fbex.flixd.backend.common.parseAs
import io.fbex.flixd.backend.common.queryParameters
import okhttp3.OkHttpClient
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class TmdbService(private val properties: TmdbProperties) {

    private val httpClient = OkHttpClient()
    private val movieUrl = "${properties.url}/search/movie"

    fun searchMovie(query: String): TmdbSearchResult {
        val response = httpClient.call(movieUrl) {
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
            response.isSuccessful -> response.body.parseAs(TmdbSearchResult::class.java)
            else -> throw TmdbResponseException(response.httpStatus)
        }
    }
}

class TmdbResponseException(httpStatus: HttpStatus) :
    IllegalStateException("TMDb responded with http status [$httpStatus]")
