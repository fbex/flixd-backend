package io.fbex.flixd.backend.watchlist

import io.fbex.flixd.backend.media.MediaService
import io.fbex.flixd.backend.media.model.BasicMediaItem
import io.fbex.flixd.backend.media.model.MediaId
import io.fbex.flixd.backend.watchlist.persistence.WatchlistItemRepository
import io.fbex.flixd.backend.watchlist.persistence.WatchlistRepository
import io.fbex.flixd.backend.watchlist.persistence.entity.WatchlistAlreadyContainsMediaItemException
import io.fbex.flixd.backend.watchlist.persistence.entity.WatchlistDoesNotContainMediaItemException
import io.fbex.flixd.backend.watchlist.persistence.entity.addItem
import io.fbex.flixd.backend.watchlist.persistence.entity.removeItem
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service

/**
 * Handles retrieval and persistence of [BasicMediaItem].
 */
@Service
class WatchlistService internal constructor(
    private val watchlistRepository: WatchlistRepository,
    private val itemRepository: WatchlistItemRepository,
    private val media: MediaService
) {

    /**
     * Retrieves [Watchlist] from [WatchlistRepository] by
     * a given watchlistId.
     *
     * @return [Watchlist]
     */
    fun getWatchlistById(watchlistId: Long): Watchlist? {
        return watchlistRepository.findDistinctById(watchlistId)?.toWatchlist()
    }

    /**
     * Adds a [BasicMediaItem] to a [Watchlist] by its [MediaId].
     *
     * If the media item is already persisted by another watchlist this
     * entity gets added to the given watchlist.
     * Otherwise a new media item is fetched and persisted from [MediaService].
     *
     * @return [BasicMediaItem]
     * @throws MediaItemNotFoundException
     * @throws WatchlistAlreadyContainsMediaItemException
     */
    fun addItemByMediaId(watchlistId: Long, mediaId: MediaId): BasicMediaItem {
        val mediaItem = itemRepository.findDistinctByMediaId(mediaId.toString())
            ?: media.findBasicMediaItem(mediaId)?.toMediaItemEntity()
            ?: mediaItemNotFoundError(mediaId)
        watchlistRepository.update(watchlistId) { it.addItem(mediaItem) }
        return mediaItem.toBasicMediaItem()
    }

    /**
     * Removes a [BasicMediaItem] from the given [Watchlist] by its [MediaId].
     *
     * If the media item is not referenced by any other watchlist it gets
     * removed entirely.
     *
     * @return [BasicMediaItem]
     * @throws MediaItemNotFoundException
     * @throws WatchlistDoesNotContainMediaItemException
     */
    fun removeItemByMediaId(watchlistId: Long, mediaId: MediaId): BasicMediaItem {
        val mediaItem = itemRepository.findDistinctByMediaId(mediaId.toString())
            ?: mediaItemNotFoundError(mediaId)
        watchlistRepository.update(watchlistId) { it.removeItem(mediaItem) }
        return mediaItem.toBasicMediaItem()
    }
}

private fun <T, ID> CrudRepository<T, ID>.update(id: ID, block: (T) -> Unit): T {
    val entity = findById(id).get()
    block(entity)
    return save(entity)
}

private fun mediaItemNotFoundError(mediaId: MediaId): Nothing = throw MediaItemNotFoundException(mediaId)

class MediaItemNotFoundException(mediaId: MediaId) :
    IllegalStateException("No media item found for mediaId [$mediaId]")
