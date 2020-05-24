package io.fbex.flixd.backend.media

import io.fbex.flixd.backend.media.model.MOVIE_SHAWSHANK_REDEMPTION
import io.fbex.flixd.backend.media.model.MediaSearchResult
import io.fbex.flixd.backend.media.model.BASIC_TV_SCRUBS
import io.fbex.flixd.backend.media.model.BASIC_MOVIE_SHAWSHANK_REDEMPTION
import io.fbex.flixd.backend.media.model.TV_SCRUBS
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest(MediaRestController::class)
internal class MediaRestControllerTests(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val mediaService: MediaService
) {

    private val basePath = "/media"

    @TestConfiguration
    class Configuration {
        @Bean
        fun mediaService(): MediaService = mockk()
    }

    @Test
    fun `returns status 401 UNAUTHORIZED if request is not authorized`() {
        mockMvc.get(basePath).andExpect {
            status { isUnauthorized }
        }
    }

    @Test
    @WithMockUser
    fun `returns search result`() {
        every { mediaService.search("query") } returns
                MediaSearchResult(
                    results = listOf(
                        BASIC_TV_SCRUBS,
                        BASIC_MOVIE_SHAWSHANK_REDEMPTION
                    )
                )

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
            		"popularity": 40.779,
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
            		"genreIds": [18, 80],
            		"posterPath": "/78Pb6FMLMfpm1jUOKTniwREYgAN.jpg",
            		"backdropPath": "/avedvodAZUcwqevBfm8p4G2NziQ.jpg"
            	}]
            }
            """.trimIndent()

        mockMvc.post("$basePath/search") {
            content = """{ "query": "query" }"""
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { json(expectedJson) }
        }
    }

    @Test
    @WithMockUser
    fun `gets a movie`() {
        every { mediaService.findMovie(278) } returns MOVIE_SHAWSHANK_REDEMPTION

        val expectedJson = """
            {
                "title": "Die Verurteilten",
                "originalTitle": "The Shawshank Redemption",
                "releaseDate": "1994-09-23",
                "tmdbId": 278,
                "imdbId": "tt0111161",
                "voteAverage": 8.7,
                "voteCount": 16098,
                "popularity": 45.386,
                "genres": [{
                    "id": 18,
                    "name": "Drama"
                }, {
                    "id": 80,
                    "name": "Krimi"
                }],
                "originalLanguage": "en",
                "posterPath": "/78Pb6FMLMfpm1jUOKTniwREYgAN.jpg",
                "backdropPath": "/avedvodAZUcwqevBfm8p4G2NziQ.jpg"
            }
            """.trimIndent()

        mockMvc.get("$basePath/movie/278")
            .andExpect {
                status { isOk }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json(expectedJson) }
            }
    }

    @Test
    @WithMockUser
    fun `returns status 404 if a movie is not found`() {
        every { mediaService.findMovie(420) } returns null

        mockMvc.get("$basePath/movie/420")
            .andExpect {
                status { isNotFound }
            }
    }

    @Test
    @WithMockUser
    fun `gets a tv show`() {
        every { mediaService.findTvShow(4556) } returns TV_SCRUBS

        val expectedJson = """
            {
            	"name": "Scrubs - Die Anfänger",
            	"originalName": "Scrubs",
            	"firstAirDate": "2001-10-02",
            	"lastAirDate": "2010-03-17",
            	"tmdbId": 4556,
            	"voteAverage": 7.9,
            	"voteCount": 768,
            	"popularity": 40.779,
            	"genres": [{
            		"id": 35,
            		"name": "Komödie"
            	}],
            	"originalLanguage": "en",
            	"posterPath": "/u1z05trCA7AuSuDhi365grwdos1.jpg",
            	"backdropPath": "/sVaCswyCaBdCMIfClV1caOBCoKT.jpg"
            }
            """.trimIndent()

        mockMvc.get("$basePath/tv/4556")
            .andExpect {
                status { isOk }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json(expectedJson) }
            }
    }

    @Test
    @WithMockUser
    fun `returns status 404 if a tv show is not found`() {
        every { mediaService.findTvShow(711) } returns null

        mockMvc.get("$basePath/tv/711")
            .andExpect {
                status { isNotFound }
            }
    }
}
