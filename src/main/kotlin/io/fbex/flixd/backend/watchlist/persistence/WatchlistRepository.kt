package io.fbex.flixd.backend.watchlist.persistence

import io.fbex.flixd.backend.watchlist.persistence.entity.WatchlistEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
internal interface WatchlistRepository : CrudRepository<WatchlistEntity, Long> {
    fun findDistinctById(id: Long): WatchlistEntity?
}
