package io.fbex.flixd.backend.tmdb

import io.mockk.coEvery
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient

@ExtendWith(SpringExtension::class)
@WebFluxTest(controllers = [TmdbRestController::class])
internal class TmdbRestControllerTests(
    @Autowired private val webClient: WebTestClient,
    @Autowired private val tmdbService: TmdbService
) {

    @TestConfiguration
    class TmdbRestControllerTestsConfiguration {
        @Bean
        fun tmdbService(): TmdbService = mockk()
    }

    @Test
    fun `search movie`() {
        val query = "pulp fiction"
        val searchResult = TmdbSearchResult(
            page = 1,
            total_pages = 1,
            total_results = 1,
            results = listOf(MOVIE_PULP_FICTION_SEARCH)
        )
        val expectedJson = """
            {
            	"page": 1,
            	"total_results": 1,
            	"total_pages": 1,
            	"results": [{
            		"id": 680,
            		"title": "Pulp Fiction",
            		"vote_average": 8.5,
            		"vote_count": 17326,
            		"video": false,
            		"popularity": 28.514,
            		"poster_path": "/d5iIlFn5s0ImszYzBPb8JPIfbXD.jpg",
            		"original_language": "en",
            		"original_title": "Pulp Fiction",
            		"genre_ids": [80, 53],
            		"backdrop_path": "/suaEOtk1N1sgg2MTM7oZd2cfVp3.jpg",
            		"adult": false,
            		"overview": "A burger-loving hit man, his philosophical partner, a drug-addled gangster's moll and a washed-up boxer converge in this sprawling, comedic crime caper. Their adventures unfurl in three stories that ingeniously trip back and forth in time.",
            		"release_date": "1994-09-10"
            	}]
            }
        """.trimIndent()

        coEvery { tmdbService.search(query) } returns searchResult

        webClient.post().uri("/tmdb/search")
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON)
            .bodyValue(SearchRequest(query))
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .json(expectedJson)
    }

    @Test
    fun `get movie by id`() {
        val id = 680
        val expectedJson = """
            {
            	"adult": false,
            	"backdrop_path": "/suaEOtk1N1sgg2MTM7oZd2cfVp3.jpg",
            	"belongs_to_collection": null,
            	"budget": 8000000,
            	"genres": [{
            		"id": 53,
            		"name": "Thriller"
            	}, {
            		"id": 80,
            		"name": "Crime"
            	}],
            	"homepage": "",
            	"id": 680,
            	"imdb_id": "tt0110912",
            	"original_language": "en",
            	"original_title": "Pulp Fiction",
            	"overview": "A burger-loving hit man, his philosophical partner, a drug-addled gangster's moll and a washed-up boxer converge in this sprawling, comedic crime caper. Their adventures unfurl in three stories that ingeniously trip back and forth in time.",
            	"popularity": 28.514,
            	"poster_path": "/d5iIlFn5s0ImszYzBPb8JPIfbXD.jpg",
            	"production_companies": [{
            		"id": 14,
            		"logo_path": "/m6AHu84oZQxvq7n1rsvMNJIAsMu.png",
            		"name": "Miramax",
            		"origin_country": "US"
            	}, {
            		"id": 59,
            		"logo_path": "/yH7OMeSxhfP0AVM6iT0rsF3F4ZC.png",
            		"name": "A Band Apart",
            		"origin_country": "US"
            	}, {
            		"id": 216,
            		"logo_path": null,
            		"name": "Jersey Films",
            		"origin_country": ""
            	}],
            	"production_countries": [{
            		"iso_3166_1": "US",
            		"name": "United States of America"
            	}],
            	"release_date": "1994-09-10",
            	"revenue": 214179088,
            	"runtime": 154,
            	"spoken_languages": [{
            		"iso_639_1": "en",
            		"name": "English"
            	}, {
            		"iso_639_1": "es",
            		"name": "Español"
            	}, {
            		"iso_639_1": "fr",
            		"name": "Français"
            	}],
            	"status": "Released",
            	"tagline": "Just because you are a character doesn't mean you have character.",
            	"title": "Pulp Fiction",
            	"video": false,
            	"vote_average": 8.5,
            	"vote_count": 17331
            }
        """.trimIndent()

        coEvery { tmdbService.getMovieDetails(id) } returns MOVIE_PULP_FICTION_DETAILS

        webClient.get().uri("/tmdb/movie/$id")
            .accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .json(expectedJson)
    }

    @Test
    fun `responds with status 404 NOT_FOUND if movie isn't found`() {
        val id = 42

        coEvery { tmdbService.getMovieDetails(id) } returns null

        webClient.get().uri("/tmdb/movie/$id")
            .accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound
    }
}
