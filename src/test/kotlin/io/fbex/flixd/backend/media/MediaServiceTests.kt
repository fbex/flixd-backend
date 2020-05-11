package io.fbex.flixd.backend.media

import io.fbex.flixd.backend.media.model.MOVIE_SHAWSHANK_REDEMPTION
import io.fbex.flixd.backend.media.model.MediaId
import io.fbex.flixd.backend.media.model.MediaSearchResult
import io.fbex.flixd.backend.media.model.BASIC_MOVIE_SHAWSHANK_REDEMPTION
import io.fbex.flixd.backend.media.model.BASIC_TV_SCRUBS
import io.fbex.flixd.backend.media.model.TV_SCRUBS
import io.fbex.flixd.backend.media.tmdb.TmdbAccessor
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class MediaServiceTests {

    private val tmdbAccessor: TmdbAccessor = mockk()
    private val testee = MediaService(tmdbAccessor)

    @Test
    fun `search calls tmdb`() {
        val expectedResult = MediaSearchResult(results = listOf(BASIC_MOVIE_SHAWSHANK_REDEMPTION))
        every { tmdbAccessor.search("query") } returns expectedResult

        val actualResult = testee.search("query")

        assertThat(actualResult).isEqualTo(expectedResult)
    }

    @Test
    fun `find movie calls tmdb`() {
        every { tmdbAccessor.findMovie(420) } returns MOVIE_SHAWSHANK_REDEMPTION

        val movie = testee.findMovie(420)

        assertThat(movie).isEqualTo(MOVIE_SHAWSHANK_REDEMPTION)
    }

    @Test
    fun `find tv show calls tmdb`() {
        every { tmdbAccessor.findTvShow(711) } returns TV_SCRUBS

        val movie = testee.findTvShow(711)

        assertThat(movie).isEqualTo(TV_SCRUBS)
    }

    @Test
    fun `find basic movie`() {
        every { tmdbAccessor.findMovie(420) } returns MOVIE_SHAWSHANK_REDEMPTION

        val basicMediaItem = testee.findBasicMediaItem(MediaId("movie-420"))

        assertThat(basicMediaItem).isEqualTo(BASIC_MOVIE_SHAWSHANK_REDEMPTION)
    }

    @Test
    fun `find basic tv show`() {
        every { tmdbAccessor.findTvShow(711) } returns TV_SCRUBS

        val basicMediaItem = testee.findBasicMediaItem(MediaId("tv-711"))

        assertThat(basicMediaItem).isEqualTo(BASIC_TV_SCRUBS)
    }
}
