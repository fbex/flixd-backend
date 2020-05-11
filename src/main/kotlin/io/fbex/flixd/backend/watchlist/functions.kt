package io.fbex.flixd.backend.watchlist

import io.fbex.flixd.backend.media.model.BasicMediaItem
import io.fbex.flixd.backend.media.model.MediaId
import io.fbex.flixd.backend.media.model.MediaType
import io.fbex.flixd.backend.watchlist.persistence.entity.MediaItemEntity
import io.fbex.flixd.backend.watchlist.persistence.entity.WatchlistEntity

internal fun WatchlistEntity.toWatchlist() = Watchlist(
    id = id,
    items = items.map { it.mediaItem.toBasicMediaItem() }
)

internal fun MediaItemEntity.toBasicMediaItem(): BasicMediaItem {
    val mediaType = type.toMediaType()
    return BasicMediaItem(
        mediaId = MediaId(tmdbId, mediaType),
        tmdbId = tmdbId,
        imdbId = imdbId,
        type = mediaType,
        title = title,
        originalTitle = originalTitle,
        originalLanguage = originalLanguage,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity,
        genreIds = emptyList(), // TODO: persist
        posterPath = posterPath,
        backdropPath = backdropPath
    )
}

internal fun BasicMediaItem.toMediaItemEntity() = MediaItemEntity(
    mediaId = mediaId.toString(),
    tmdbId = tmdbId,
    imdbId = imdbId,
    type = type.toMediaItemEntity(),
    title = title,
    originalTitle = originalTitle,
    originalLanguage = originalLanguage,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    voteCount = voteCount,
    popularity = popularity,
    posterPath = posterPath,
    backdropPath = backdropPath,
    watchlistItems = mutableSetOf(),
    vodItems = mutableSetOf()
)

internal fun MediaItemEntity.Type.toMediaType(): MediaType = when (this) {
    MediaItemEntity.Type.Movie -> MediaType.Movie
    MediaItemEntity.Type.TvShow -> MediaType.TvShow
}

internal fun MediaType.toMediaItemEntity(): MediaItemEntity.Type = when (this) {
    MediaType.Movie -> MediaItemEntity.Type.Movie
    MediaType.TvShow -> MediaItemEntity.Type.TvShow
}
