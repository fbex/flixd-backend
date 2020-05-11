package io.fbex.flixd.backend.watchlist.persistence.entity

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "watchlist_items")
internal class WatchlistItemEntity(
    @ManyToOne
    @JoinColumn(name = "watchlist_id")
    var watchlist: WatchlistEntity,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "media_id")
    var mediaItem: MediaItemEntity
) : Auditable() {
    @Id
    @GeneratedValue
    val id: Long = 0
}
