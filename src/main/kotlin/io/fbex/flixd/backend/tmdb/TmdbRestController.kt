package io.fbex.flixd.backend.tmdb

import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tmdb")
class TmdbRestController(
    private val tmdb: TmdbService
) {

    @PostMapping("/search")
    suspend fun search(@RequestBody request: SearchRequest): TmdbSearchResult {
        return tmdb.search(request.query)
    }

    @GetMapping("/movie/{id}")
    suspend fun getMovieById(@PathVariable id: Int): ResponseEntity<TmdbMovieDetails> {
        val movie = tmdb.getMovieDetails(id) ?: return notFound()
        return ok(movie)
    }
}

data class SearchRequest(
    val query: String
)

private fun <T> notFound() = ResponseEntity.notFound().build<T>()
