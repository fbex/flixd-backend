package io.fbex.flixd.backend.media

import io.fbex.flixd.backend.media.model.MediaSearchResult
import io.fbex.flixd.backend.media.model.Movie
import io.fbex.flixd.backend.media.model.TvShow
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/media")
class MediaRestController(private val media: MediaService) {

    @PostMapping(
        "/search",
        consumes = [APPLICATION_JSON_VALUE],
        produces = [APPLICATION_JSON_VALUE]
    )
    fun search(@RequestBody request: SearchRequest): MediaSearchResult =
        media.search(request.query)

    @GetMapping(
        "/movie/{tmdbId}",
        produces = [APPLICATION_JSON_VALUE]
    )
    fun getMovie(@PathVariable tmdbId: Int): ResponseEntity<Movie> {
        val movie = media.findMovie(tmdbId) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(movie)
    }

    @GetMapping(
        "/tv/{tmdbId}",
        produces = [APPLICATION_JSON_VALUE]
    )
    fun getTvShow(@PathVariable tmdbId: Int): ResponseEntity<TvShow> {
        val tvShow = media.findTvShow(tmdbId) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(tvShow)
    }
}

data class SearchRequest(val query: String)
