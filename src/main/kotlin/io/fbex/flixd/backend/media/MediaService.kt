package io.fbex.flixd.backend.media

import io.fbex.flixd.backend.media.model.BasicMediaItem
import io.fbex.flixd.backend.media.model.MediaId
import io.fbex.flixd.backend.media.model.MediaSearchResult
import io.fbex.flixd.backend.media.model.MediaType
import io.fbex.flixd.backend.media.model.Movie
import io.fbex.flixd.backend.media.model.TvShow
import io.fbex.flixd.backend.media.model.toBasicMediaItem
import io.fbex.flixd.backend.media.tmdb.TmdbAccessor
import org.springframework.stereotype.Service

/**
 * A facade for accessing media from outside this package.
 * Abstracts from the actual media backend (TMDB).
 */
@Service
class MediaService internal constructor(private val tmdbAccessor: TmdbAccessor) {

    /**
     * Search for movies or tv shows in a single request.
     *
     * The [SearchResults][MediaSearchResult] are sorted
     * descending by popularity.
     *
     * @return [MediaSearchResult]
     */
    fun search(query: String): MediaSearchResult = tmdbAccessor.search(query)

    /**
     * Find a [Movie] by its TMDB-ID.
     *
     * @return [Movie]
     */
    fun findMovie(tmdbId: Int): Movie? = tmdbAccessor.findMovie(tmdbId)

    /**
     * Find a [TvShow] by its TMDB-ID.
     *
     * @return [TvShow]
     */
    fun findTvShow(tmdbId: Int): TvShow? = tmdbAccessor.findTvShow(tmdbId)

    /**
     * Find a [BasicMediaItem] by its [MediaId].
     * This can either be [Movie] or a [TvShow].
     *
     * @param mediaId of type [MediaId]
     * @return [TvShow]
     */
    fun findBasicMediaItem(mediaId: MediaId): BasicMediaItem? = when (mediaId.mediaType) {
        MediaType.Movie -> findMovie(mediaId.tmdbId)?.toBasicMediaItem()
        MediaType.TvShow -> findTvShow(mediaId.tmdbId)?.toBasicMediaItem()
    }
}
