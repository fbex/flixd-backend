package io.fbex.flixd.backend.watchlist

import io.fbex.flixd.backend.media.model.BASIC_MOVIE_SHAWSHANK_REDEMPTION
import io.fbex.flixd.backend.media.model.BASIC_MOVIE_SILENCE_OF_THE_LAMBS
import io.fbex.flixd.backend.media.model.MEDIA_ID_MOVIE_SHAWSHANK_REDEPMTION
import io.fbex.flixd.backend.media.model.MEDIA_ID_MOVIE_SILENCE_OF_THE_LAMBS
import io.fbex.flixd.backend.watchlist.persistence.entity.WatchlistAlreadyContainsMediaItemException
import io.fbex.flixd.backend.watchlist.persistence.entity.WatchlistDoesNotContainMediaItemException
import io.fbex.flixd.backend.watchlist.persistence.entity.WatchlistEntity
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest(WatchlistRestController::class)
internal class WatchlistRestControllerTests @Autowired constructor(
    private val mockMvc: MockMvc,
    private val service: WatchlistService
) {

    private val basePath = "/watchlist"
    private val watchlistId: Long = 1
    private val watchlist = Watchlist(
        id = watchlistId,
        items = listOf(BASIC_MOVIE_SILENCE_OF_THE_LAMBS)
    )

    @TestConfiguration
    class Configuration {
        @Bean
        fun watchlistService(): WatchlistService = mockk()
    }

    @Test
    fun `returns status 401 UNAUTHORIZED if request is not authorized`() {
        mockMvc.get(basePath).andExpect {
            status { isUnauthorized }
        }
    }

    @Nested
    @DisplayName("get watchlist")
    inner class GetWatchlist {

        @Test
        @WithMockUser
        fun `retrieves a watchlist from the service`() {
            val expectedJson = """
                {
                    "id": 1,
                    "items": [{
                        "mediaId": "movie-274",
                        "tmdbId": 274,
                        "imdbId": "tt0102926",
                        "type": "Movie",
                        "title": "Das Schweigen der Lämmer",
                        "originalTitle": "The Silence of the Lambs",
                        "originalLanguage": "en",
                        "releaseDate": "1991-02-01",
                        "voteAverage": 8.3,
                        "voteCount": 10146,
                        "popularity": 6.51,
                        "genreIds": [80, 18, 53, 27],
                        "posterPath": "/rln3cSeqRusXZLjfWKdXh1c7C06.jpg",
                        "backdropPath": "/pI9B4wppGflpsOD2T6rxgC5Clmz.jpg"
                    }]
                }
            """.trimIndent()

            every { service.getWatchlistById(watchlistId) } returns watchlist

            mockMvc.get(basePath).andExpect {
                status { isOk }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json(expectedJson) }
            }
        }

        @Test
        @WithMockUser
        fun `returns status 404 NOT_FOUND if requested watchlist is not found`() {
            every { service.getWatchlistById(watchlistId) } returns null

            mockMvc.get(basePath).andExpect {
                status { isNotFound }
            }
        }
    }

    @Nested
    @DisplayName("add item to watchlist")
    inner class AddItem {

        private val addItemUrl = "$basePath/item/$MEDIA_ID_MOVIE_SHAWSHANK_REDEPMTION"

        @Test
        @WithMockUser
        fun `adds an item to the watchlist`() {
            val expectedJson = """
                {
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
            	}
            """.trimIndent()
            every { service.addItemByMediaId(watchlistId, MEDIA_ID_MOVIE_SHAWSHANK_REDEPMTION) } returns
                    BASIC_MOVIE_SHAWSHANK_REDEMPTION

            mockMvc.post(addItemUrl).andExpect {
                status { isOk }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json(expectedJson) }
            }
        }

        @Test
        @WithMockUser
        fun `returns status 422 UNPROCESSABLE_ENTITY if the item is not found`() {
            every { service.addItemByMediaId(watchlistId, MEDIA_ID_MOVIE_SHAWSHANK_REDEPMTION) } throws
                    MediaItemNotFoundException(MEDIA_ID_MOVIE_SHAWSHANK_REDEPMTION)

            mockMvc.post(addItemUrl).andExpect {
                status { isUnprocessableEntity }
            }
        }

        @Test
        @WithMockUser
        fun `returns status 422 UNPROCESSABLE_ENTITY if the item is already present in the watchlist`() {
            val watchlistEntity = WatchlistEntity(items = mutableSetOf())
            val mediaItemEntity = BASIC_MOVIE_SHAWSHANK_REDEMPTION.toMediaItemEntity()
            every { service.addItemByMediaId(watchlistId, MEDIA_ID_MOVIE_SHAWSHANK_REDEPMTION) } throws
                    WatchlistAlreadyContainsMediaItemException(watchlistEntity, mediaItemEntity)

            mockMvc.post(addItemUrl).andExpect {
                status { isUnprocessableEntity }
            }
        }
    }

    @Nested
    @DisplayName("remove item from watchlist")
    inner class RemoveItem {

        private val removeItemUrl = "$basePath/item/$MEDIA_ID_MOVIE_SILENCE_OF_THE_LAMBS"

        @Test
        @WithMockUser
        fun `removes an item from the watchlist`() {
            val expectedJson = """
                {
                    "mediaId": "movie-274",
                    "tmdbId": 274,
                    "imdbId": "tt0102926",
                    "type": "Movie",
                    "title": "Das Schweigen der Lämmer",
                    "originalTitle": "The Silence of the Lambs",
                    "originalLanguage": "en",
                    "releaseDate": "1991-02-01",
                    "voteAverage": 8.3,
                    "voteCount": 10146,
                    "popularity": 6.51,
                    "genreIds": [80, 18, 53, 27],
                    "posterPath": "/rln3cSeqRusXZLjfWKdXh1c7C06.jpg",
                    "backdropPath": "/pI9B4wppGflpsOD2T6rxgC5Clmz.jpg"
                }
            """.trimIndent()
            every { service.removeItemByMediaId(watchlistId, MEDIA_ID_MOVIE_SILENCE_OF_THE_LAMBS) } returns
                    BASIC_MOVIE_SILENCE_OF_THE_LAMBS

            mockMvc.delete(removeItemUrl).andExpect {
                status { isOk }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json(expectedJson) }
            }
        }

        @Test
        @WithMockUser
        fun `returns status 422 UNPROCESSABLE_ENTITY if the item is not found`() {
            every { service.addItemByMediaId(watchlistId, MEDIA_ID_MOVIE_SILENCE_OF_THE_LAMBS) } throws
                    MediaItemNotFoundException(MEDIA_ID_MOVIE_SILENCE_OF_THE_LAMBS)

            mockMvc.post(removeItemUrl).andExpect {
                status { isUnprocessableEntity }
            }
        }

        @Test
        @WithMockUser
        fun `returns status 422 UNPROCESSABLE_ENTITY if the item is not present in the watchlist`() {
            val watchlistEntity = WatchlistEntity(items = mutableSetOf())
            val mediaItemEntity = BASIC_MOVIE_SILENCE_OF_THE_LAMBS.toMediaItemEntity()
            every { service.addItemByMediaId(watchlistId, MEDIA_ID_MOVIE_SILENCE_OF_THE_LAMBS) } throws
                    WatchlistDoesNotContainMediaItemException(watchlistEntity, mediaItemEntity)

            mockMvc.post(removeItemUrl).andExpect {
                status { isUnprocessableEntity }
            }
        }
    }
}
