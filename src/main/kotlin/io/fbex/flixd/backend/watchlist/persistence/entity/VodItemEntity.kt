package io.fbex.flixd.backend.watchlist.persistence.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "vod_items")
internal class VodItemEntity(
    var provider: String
) {
    @Id
    @GeneratedValue
    val id: Long = 0
}
