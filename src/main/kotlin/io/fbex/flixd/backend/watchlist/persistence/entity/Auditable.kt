package io.fbex.flixd.backend.watchlist.persistence.entity

import java.time.Instant
import javax.persistence.MappedSuperclass
import javax.persistence.PrePersist
import javax.persistence.PreUpdate

@MappedSuperclass
internal abstract class Auditable {

    var createdAt: Instant = Instant.now()
    var updatedAt: Instant = Instant.now()

    @PrePersist
    fun onPersist() {
        val now = Instant.now()
        createdAt = now
        updatedAt = now
    }

    @PreUpdate
    fun onUpdate() {
        updatedAt = Instant.now()
    }
}
