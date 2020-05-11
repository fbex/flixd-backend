package io.fbex.flixd.backend.watchlist.persistence.entity

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.OrderBy
import javax.persistence.Table

@Entity
@Table(name = "watchlists")
internal class WatchlistEntity(
    @OneToMany(
        mappedBy = "watchlist",
        fetch = FetchType.EAGER,
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    @OrderBy("created_at")
    val items: MutableSet<WatchlistItemEntity>
) : Auditable() {
    @Id
    @GeneratedValue
    var id: Long = 0
}

internal fun WatchlistEntity.addItem(mediaItem: MediaItemEntity) {
    if (this.items.containsMediaItem(mediaItem)) throw WatchlistAlreadyContainsMediaItemException(this, mediaItem)
    val watchlistItem = WatchlistItemEntity(this, mediaItem)
    this.items.add(watchlistItem)
}

internal fun WatchlistEntity.removeItem(mediaItem: MediaItemEntity) {
    val watchlistItem = this.items.find { it.mediaItem.id == mediaItem.id }
        ?: throw WatchlistDoesNotContainMediaItemException(this, mediaItem)
    this.items.remove(watchlistItem)
}

private fun Set<WatchlistItemEntity>.containsMediaItem(mediaItem: MediaItemEntity): Boolean =
    find { it.mediaItem.mediaId == mediaItem.mediaId } != null

class WatchlistAlreadyContainsMediaItemException internal constructor(watchlist: WatchlistEntity, mediaItem: MediaItemEntity) :
    IllegalStateException("Watchlist [${watchlist.id}] already contains MediaItem with mediaId [${mediaItem.mediaId}]")

class WatchlistDoesNotContainMediaItemException internal constructor(watchlist: WatchlistEntity, mediaItem: MediaItemEntity) :
    IllegalStateException("Watchlist [${watchlist.id}] does not contain MediaItem with mediaId [${mediaItem.mediaId}]")
