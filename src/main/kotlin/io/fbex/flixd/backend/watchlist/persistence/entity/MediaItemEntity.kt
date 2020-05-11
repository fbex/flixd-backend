package io.fbex.flixd.backend.watchlist.persistence.entity

import java.time.LocalDate
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "media_items")
internal class MediaItemEntity(
    var mediaId: String,
    var tmdbId: Int,
    var imdbId: String?,
    var type: Type,
    var title: String,
    var originalTitle: String,
    var originalLanguage: String,
    var releaseDate: LocalDate?,
    var voteAverage: Double,
    var voteCount: Int,
    var popularity: Double,
    var posterPath: String?,
    var backdropPath: String?,

    @OneToMany(
        mappedBy = "mediaItem",
        fetch = FetchType.EAGER,
        cascade = [CascadeType.MERGE]
    )
    var watchlistItems: MutableSet<WatchlistItemEntity>,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "media_item_id")
    var vodItems: MutableSet<VodItemEntity>
) : Auditable() {
    @Id
    @GeneratedValue
    val id: Long = 0

    enum class Type {
        Movie, TvShow
    }
}
