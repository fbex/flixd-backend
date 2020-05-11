package io.fbex.flixd.backend.watchlist

import io.fbex.flixd.backend.media.model.BasicMediaItem

data class Watchlist(
    val id: Long,
    val items: List<BasicMediaItem>
)
