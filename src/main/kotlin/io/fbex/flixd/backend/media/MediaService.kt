package io.fbex.flixd.backend.media

import io.fbex.flixd.backend.media.tmdb.TmdbAccessor
import org.springframework.stereotype.Service

@Service
class MediaService internal constructor(private val tmdbAccessor: TmdbAccessor) {

    fun search(query: String) = tmdbAccessor.search(query)

    fun findMovie(tmdbId: Int) = tmdbAccessor.findMovie(tmdbId)
}
