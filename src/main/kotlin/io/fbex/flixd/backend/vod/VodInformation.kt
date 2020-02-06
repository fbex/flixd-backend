package io.fbex.flixd.backend.vod

/**
 * All information collected for VOD streaming.
 *
 * This contains:
 *  - A list of offers, i.e. where a certain title is available for streaming
 *  - Some further information about a title (i.e. imdb, tomato, metacritic scores)
 */
data class VodInformation(
    val justWatchId: Int,
    val title: String,
    val year: Int,
    val tmdbId: Int?,
    val tomatoId: Int?,
    val offers: List<Offer>
) {

    /**
     * Verify if the search result equals the requested data.
     *
     * If both request and result contain a "tmdbId", detection can be verified safely.
     * Otherwise checks of the "title" and "year" should provide a good enough guess.
     */
    fun matchesQuery(tmdbId: Int, title: String, year: Int): Boolean {
        // if the result contains a tmdbId, a comparison shows a definitive match
        if (this.tmdbId != null) {
            return this.tmdbId == tmdbId
        }

        // if the tmdbId cannot be compared, the other parameters have to match
        if (this.title.toUpperCase() != title.toUpperCase()) return false
        if (this.year != year) return false
        return true
    }
}
