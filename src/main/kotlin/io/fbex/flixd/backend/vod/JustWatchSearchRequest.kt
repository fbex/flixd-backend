package io.fbex.flixd.backend.vod

import com.fasterxml.jackson.annotation.JsonValue

/**
 * Request for finding a movie or a TV-show on JustWatch.
 *
 * It contains:
 *  - a query string, which should be the requested title
 *  - the release year of the title
 *  - a list of content types ("movie", "show")
 *  - the requested page size (defaults to 1)
 *
 *
 *  Additional request options, that are not needed with the current
 *  implementation are:
 *
 *  providers:              List<String>?
 *  monetization_types:     List<String>?
 *  presentation_types:     String?
 *  genres:                 List<String>?
 *  languages:              List<String>?
 *  min_price:              String?
 *  max_price:              String?
 *  scoring_filter_types:   List<String>?
 *  cinema_release:         Int?
 *  age_certifications:     List<String>?
 *  page:                   Int?
 *  timeline_type:          String?
 *  person_id:              Int?
 *  nationwide_cinema_releases_only: Boolean
 */
data class JustWatchSearchRequest(
    val query: String,
    val release_year_from: Int?,
    val release_year_until: Int?,
    val content_types: List<JustWatchContentType>?,
    val page_size: Int
)

enum class JustWatchContentType(@JsonValue val value: String) {
    MOVIE("movie"), TV_SHOW("show")
}
