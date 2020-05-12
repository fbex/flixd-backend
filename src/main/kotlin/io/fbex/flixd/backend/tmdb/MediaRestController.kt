package io.fbex.flixd.backend.tmdb

import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/media")
class MediaRestController(private val tmdbService: TmdbService) {

    @PostMapping(
        "/search",
        consumes = [APPLICATION_JSON_VALUE],
        produces = [APPLICATION_JSON_VALUE]
    )
    fun search(@RequestBody request: SearchRequest): MediaSearchResult =
        tmdbService.search(request.query)
}

data class SearchRequest(val query: String)
