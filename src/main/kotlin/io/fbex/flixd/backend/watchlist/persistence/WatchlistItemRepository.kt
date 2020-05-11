package io.fbex.flixd.backend.watchlist.persistence

import io.fbex.flixd.backend.watchlist.persistence.entity.MediaItemEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
internal interface WatchlistItemRepository : CrudRepository<MediaItemEntity, Long> {
    fun findDistinctByMediaId(mediaId: String): MediaItemEntity?
}
