package io.fbex.flixd.backend.watchlist

import io.fbex.flixd.backend.media.MediaService
import io.fbex.flixd.backend.media.model.BASIC_MOVIE_SHAWSHANK_REDEMPTION
import io.fbex.flixd.backend.media.model.BASIC_MOVIE_SILENCE_OF_THE_LAMBS
import io.fbex.flixd.backend.media.model.MEDIA_ID_MOVIE_SHAWSHANK_REDEPMTION
import io.fbex.flixd.backend.media.model.MEDIA_ID_MOVIE_SILENCE_OF_THE_LAMBS
import io.fbex.flixd.backend.watchlist.persistence.WatchlistItemRepository
import io.fbex.flixd.backend.watchlist.persistence.WatchlistRepository
import io.fbex.flixd.backend.watchlist.persistence.entity.MediaItemEntity
import io.fbex.flixd.backend.watchlist.persistence.entity.WatchlistAlreadyContainsMediaItemException
import io.fbex.flixd.backend.watchlist.persistence.entity.WatchlistDoesNotContainMediaItemException
import io.fbex.flixd.backend.watchlist.persistence.entity.WatchlistEntity
import io.fbex.flixd.backend.watchlist.persistence.entity.WatchlistItemEntity
import io.fbex.flixd.backend.watchlist.persistence.entity.addItem
import io.mockk.Called
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.api.ObjectAssert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
internal class WatchlistServiceTests @Autowired constructor(
    private val watchlistRepository: WatchlistRepository,
    private val itemRepository: WatchlistItemRepository
) {
    private lateinit var watchlist: WatchlistEntity
    private lateinit var itemSilenceOfTheLambs: MediaItemEntity

    private val mediaService: MediaService = mockk()
    private val testee = WatchlistService(watchlistRepository, itemRepository, mediaService)

    @BeforeEach
    fun setUp() {
        watchlistRepository.deleteAll()
        itemRepository.deleteAll()
        itemSilenceOfTheLambs = itemRepository.save(BASIC_MOVIE_SILENCE_OF_THE_LAMBS.toMediaItemEntity())
        watchlist = addWatchlistWithMediaItem(itemSilenceOfTheLambs)
    }

    @Nested
    @DisplayName("getWatchlistById()")
    inner class GetWatchlistById {

        @Test
        fun `returns requested watchlist with all items`() {
            val retrievedWatchlist = testee.getWatchlistById(watchlist.id)!!

            assertThat(retrievedWatchlist).isEqualTo(watchlist.toWatchlist())
            assertThat(retrievedWatchlist.items).hasSize(1)
            assertThat(retrievedWatchlist.items.first()).isEqualToIgnoringUnmapped(BASIC_MOVIE_SILENCE_OF_THE_LAMBS)
            assertThat(watchlistRepository.findAll()).hasSize(1)
            assertThat(itemRepository.findAll()).hasSize(1)
        }

        @Test
        fun `returns null if watchlist is not found`() {
            val retrievedWatchlist = testee.getWatchlistById(420)

            assertThat(retrievedWatchlist).isNull()
        }
    }

    @Nested
    @DisplayName("addItemByMediaId()")
    inner class AddItemByMediaId {

        @Test
        fun `adds an item to the watchlist found in the database`() {
            itemRepository.save(BASIC_MOVIE_SHAWSHANK_REDEMPTION.toMediaItemEntity())

            testee.addItemByMediaId(watchlist.id, MEDIA_ID_MOVIE_SHAWSHANK_REDEPMTION)

            val retrievedWatchlist = testee.getWatchlistById(watchlist.id)!!
            assertThat(retrievedWatchlist.items).hasSize(2)
            assertThat(retrievedWatchlist.items.elementAt(0)).isEqualToIgnoringUnmapped(BASIC_MOVIE_SILENCE_OF_THE_LAMBS)
            assertThat(retrievedWatchlist.items.elementAt(1)).isEqualToIgnoringUnmapped(BASIC_MOVIE_SHAWSHANK_REDEMPTION)
            assertThat(watchlistRepository.findAll()).hasSize(1)
            assertThat(itemRepository.findAll()).hasSize(2)

            verify { mediaService wasNot Called }
            confirmVerified(mediaService)
        }

        @Test
        fun `adds an item to the watchlist found from MediaService`() {
            every { mediaService.findBasicMediaItem(MEDIA_ID_MOVIE_SHAWSHANK_REDEPMTION) } returns
                    BASIC_MOVIE_SHAWSHANK_REDEMPTION

            testee.addItemByMediaId(watchlist.id, MEDIA_ID_MOVIE_SHAWSHANK_REDEPMTION)

            val retrievedWatchlist = testee.getWatchlistById(watchlist.id)!!
            assertThat(retrievedWatchlist.items).hasSize(2)
            assertThat(retrievedWatchlist.items.elementAt(0)).isEqualToIgnoringUnmapped(BASIC_MOVIE_SILENCE_OF_THE_LAMBS)
            assertThat(retrievedWatchlist.items.elementAt(1)).isEqualToIgnoringUnmapped(BASIC_MOVIE_SHAWSHANK_REDEMPTION)
            assertThat(watchlistRepository.findAll()).hasSize(1)
            assertThat(itemRepository.findAll()).hasSize(2)

            verify { mediaService.findBasicMediaItem(MEDIA_ID_MOVIE_SHAWSHANK_REDEPMTION) }
            confirmVerified(mediaService)
        }

        @Test
        fun `throws if a media item can neither be found in the database nor from the MediaService`() {
            every { mediaService.findBasicMediaItem(MEDIA_ID_MOVIE_SHAWSHANK_REDEPMTION) } returns null

            assertThatThrownBy { testee.addItemByMediaId(watchlist.id, MEDIA_ID_MOVIE_SHAWSHANK_REDEPMTION) }
                .isInstanceOf(MediaItemNotFoundException::class.java)
                .hasMessage("No media item found for mediaId [$MEDIA_ID_MOVIE_SHAWSHANK_REDEPMTION]")

            verify { mediaService.findBasicMediaItem(MEDIA_ID_MOVIE_SHAWSHANK_REDEPMTION) }
            confirmVerified(mediaService)
        }

        @Test
        fun `throws if a media item already belongs to watchlist`() {
            assertThatThrownBy { testee.addItemByMediaId(watchlist.id, MEDIA_ID_MOVIE_SILENCE_OF_THE_LAMBS) }
                .isInstanceOf(WatchlistAlreadyContainsMediaItemException::class.java)
                .hasMessage(
                    "Watchlist [${watchlist.id}] already contains MediaItem with " +
                            "mediaId [$MEDIA_ID_MOVIE_SILENCE_OF_THE_LAMBS]"
                )

            verify { mediaService wasNot Called }
            confirmVerified(mediaService)
        }
    }

    @Nested
    @DisplayName("removeItemByMediaId()")
    inner class RemoveItemByMediaId {

        @Test
        fun `removes an item from the watchlist and deletes the item if it is not referenced in any other watchlist`() {
            testee.removeItemByMediaId(watchlist.id, MEDIA_ID_MOVIE_SILENCE_OF_THE_LAMBS)

            val retrievedWatchlist = testee.getWatchlistById(watchlist.id)!!
            assertThat(retrievedWatchlist.items).isEmpty()
            assertThat(watchlistRepository.findAll()).hasSize(1)
            assertThat(itemRepository.findAll()).isEmpty()

            confirmVerified(mediaService)
        }

        @Test
        fun `removes an item from the watchlist and keeps the item if it is referenced in another watchlist`() {
            addWatchlistWithMediaItem(itemSilenceOfTheLambs)

            testee.removeItemByMediaId(watchlist.id, MEDIA_ID_MOVIE_SILENCE_OF_THE_LAMBS)

            val retrievedWatchlist = testee.getWatchlistById(watchlist.id)!!
            assertThat(retrievedWatchlist.items).isEmpty()
            assertThat(watchlistRepository.findAll()).hasSize(2)
            assertThat(itemRepository.findAll()).hasSize(1)

            confirmVerified(mediaService)
        }

        @Test
        fun `removes an item from the watchlist and adds it again`() {
            addWatchlistWithMediaItem(itemSilenceOfTheLambs)

            testee.removeItemByMediaId(watchlist.id, MEDIA_ID_MOVIE_SILENCE_OF_THE_LAMBS)

            var retrievedWatchlist = testee.getWatchlistById(watchlist.id)!!
            assertThat(retrievedWatchlist.items).isEmpty()

            testee.addItemByMediaId(watchlist.id, MEDIA_ID_MOVIE_SILENCE_OF_THE_LAMBS)

            retrievedWatchlist = testee.getWatchlistById(watchlist.id)!!
            assertThat(retrievedWatchlist.items).hasSize(1)
            assertThat(retrievedWatchlist.items.first()).isEqualToIgnoringUnmapped(BASIC_MOVIE_SILENCE_OF_THE_LAMBS)

            assertThat(watchlistRepository.findAll()).hasSize(2)
            assertThat(itemRepository.findAll()).hasSize(1)

            confirmVerified(mediaService)
        }

        @Test
        fun `removes an item from the watchlist and adds it again, retrieved from the backend`() {
            every { mediaService.findBasicMediaItem(MEDIA_ID_MOVIE_SILENCE_OF_THE_LAMBS) } returns
                    BASIC_MOVIE_SILENCE_OF_THE_LAMBS

            testee.removeItemByMediaId(watchlist.id, MEDIA_ID_MOVIE_SILENCE_OF_THE_LAMBS)

            var retrievedWatchlist = testee.getWatchlistById(watchlist.id)!!
            assertThat(retrievedWatchlist.items).isEmpty()

            testee.addItemByMediaId(watchlist.id, MEDIA_ID_MOVIE_SILENCE_OF_THE_LAMBS)

            retrievedWatchlist = testee.getWatchlistById(watchlist.id)!!
            assertThat(retrievedWatchlist.items).hasSize(1)
            assertThat(retrievedWatchlist.items.first()).isEqualToIgnoringUnmapped(BASIC_MOVIE_SILENCE_OF_THE_LAMBS)

            assertThat(watchlistRepository.findAll()).hasSize(1)
            assertThat(itemRepository.findAll()).hasSize(1)

            verify { mediaService.findBasicMediaItem(MEDIA_ID_MOVIE_SILENCE_OF_THE_LAMBS) }
            confirmVerified(mediaService)
        }

        @Test
        fun `throws if media item is not found`() {
            assertThatThrownBy { testee.removeItemByMediaId(watchlist.id, MEDIA_ID_MOVIE_SHAWSHANK_REDEPMTION) }
                .isInstanceOf(MediaItemNotFoundException::class.java)
                .hasMessage("No media item found for mediaId [$MEDIA_ID_MOVIE_SHAWSHANK_REDEPMTION]")
        }

        @Test
        fun `throws if media item is found but not associated with given watchlist`() {
            itemRepository.save(BASIC_MOVIE_SHAWSHANK_REDEMPTION.toMediaItemEntity())

            assertThatThrownBy { testee.removeItemByMediaId(watchlist.id, MEDIA_ID_MOVIE_SHAWSHANK_REDEPMTION) }
                .isInstanceOf(WatchlistDoesNotContainMediaItemException::class.java)
                .hasMessage(
                    "Watchlist [${watchlist.id}] does not contain MediaItem with " +
                            "mediaId [$MEDIA_ID_MOVIE_SHAWSHANK_REDEPMTION]"
                )
        }
    }

    @Nested
    @DisplayName("auditing dates")
    inner class AuditableDates {

        @Test
        fun `tracks created and updated dates in watchlist entities`() {
            val originalWatchlist = watchlistRepository.findById(watchlist.id).get()
            val originalCreatedDate = originalWatchlist.createdAt
            val originalUpdatedDate = originalWatchlist.updatedAt

            assertThat(originalCreatedDate).isEqualTo(originalUpdatedDate)

            originalWatchlist.addItem(BASIC_MOVIE_SHAWSHANK_REDEMPTION.toMediaItemEntity())
            watchlistRepository.save(originalWatchlist)

            val updatedWatchlist = watchlistRepository.findById(watchlist.id).get()
            val updatedCreatedDate = updatedWatchlist.createdAt
            val updatedUpdatedDate = updatedWatchlist.updatedAt

            assertThat(updatedCreatedDate).isEqualTo(originalCreatedDate)
            assertThat(updatedUpdatedDate).isEqualTo(originalUpdatedDate) // adding to items, does not change entity
        }

        @Test
        fun `tracks created date in watchlist item entities`() {
            val originalWatchlist = watchlistRepository.findById(watchlist.id).get()
            val originalItem = originalWatchlist.items.first()
            val orignalCreatedDate = originalItem.createdAt

            assertThat(orignalCreatedDate).isNotNull()
        }

        @Test
        fun `tracks created and updated dates in media item entities`() {
            val originalMediaItem =
                itemRepository.findDistinctByMediaId(MEDIA_ID_MOVIE_SILENCE_OF_THE_LAMBS.toString())!!
            val originalCreatedDate = originalMediaItem.createdAt
            val originalUpdatedDate = originalMediaItem.updatedAt

            assertThat(originalCreatedDate).isEqualTo(originalUpdatedDate)

            originalMediaItem.title = "something else"
            itemRepository.save(originalMediaItem)

            val updatedMediaItem =
                itemRepository.findDistinctByMediaId(MEDIA_ID_MOVIE_SILENCE_OF_THE_LAMBS.toString())!!
            val updatedCreatedDate = updatedMediaItem.createdAt
            val updatedUpdatedDate = updatedMediaItem.updatedAt

            assertThat(updatedCreatedDate).isEqualTo(originalCreatedDate)
            assertThat(updatedUpdatedDate).isAfter(originalUpdatedDate)
        }
    }

    private fun addWatchlistWithMediaItem(mediaItem: MediaItemEntity): WatchlistEntity {
        val otherWatchlist = WatchlistEntity(items = mutableSetOf())
        watchlistRepository.save(otherWatchlist)
        otherWatchlist.items.add(WatchlistItemEntity(otherWatchlist, mediaItem))
        return watchlistRepository.save(otherWatchlist)
    }
}

private fun <T> ObjectAssert<T>.isEqualToIgnoringUnmapped(other: T): ObjectAssert<T> =
    isEqualToIgnoringGivenFields(other, "genreIds")
