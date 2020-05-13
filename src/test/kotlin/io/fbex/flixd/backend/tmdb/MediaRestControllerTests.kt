package io.fbex.flixd.backend.tmdb

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@WebMvcTest
internal class MediaRestControllerTests(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val tmdbService: TmdbService
) {

    @TestConfiguration
    class Configuration {
        @Bean
        fun tmdbSerivce(): TmdbService = mockk()
    }

    @Test
    fun `returns search result`() {
        every { tmdbService.search("query") } returns
                MediaSearchResult(results = listOf(SEARCH_ITEM_SCRUBS, SEARCH_ITEM_SHAWSHANK_REDEMPTION))

        val expectedJson = """
            {
            	"results": [{
            		"tmdbId": 4556,
            		"type": "TvShow",
            		"title": "Scrubs - Die Anfänger",
            		"originalTitle": "Scrubs",
            		"originalLanguage": "en",
            		"releaseDate": "2001-10-02",
            		"voteAverage": 7.9,
            		"voteCount": 768,
            		"popularity": 42.175,
            		"genreIds": [35],
            		"posterPath": "/u1z05trCA7AuSuDhi365grwdos1.jpg",
            		"backdropPath": "/sVaCswyCaBdCMIfClV1caOBCoKT.jpg"
            	}, {
            		"tmdbId": 278,
            		"type": "Movie",
            		"title": "Die Verurteilten",
            		"originalTitle": "The Shawshank Redemption",
            		"originalLanguage": "en",
            		"releaseDate": "1994-09-23",
            		"voteAverage": 8.7,
            		"voteCount": 16098,
            		"popularity": 45.386,
            		"genreIds": [80, 18],
            		"posterPath": "/78Pb6FMLMfpm1jUOKTniwREYgAN.jpg",
            		"backdropPath": "/avedvodAZUcwqevBfm8p4G2NziQ.jpg"
            	}]
            }
            """.trimIndent()

        mockMvc.post("/media/search") {
            content = """{ "query": "query" }"""
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk }
            content { json(expectedJson) }
        }
    }
}
