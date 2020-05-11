package io.fbex.flixd.backend.watchlist

import io.fbex.flixd.backend.media.model.BasicMediaItem
import io.fbex.flixd.backend.media.model.MediaId
import io.fbex.flixd.backend.watchlist.persistence.entity.WatchlistAlreadyContainsMediaItemException
import io.fbex.flixd.backend.watchlist.persistence.entity.WatchlistDoesNotContainMediaItemException
import mu.KotlinLogging.logger
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/watchlist")
class WatchlistRestController(private val service: WatchlistService) {

    private val log = logger {}
    private val watchlistId: Long = 1 // TODO: get watchlist id from user/token

    @GetMapping(produces = [APPLICATION_JSON_VALUE])
    fun get(): ResponseEntity<Watchlist> {
        val watchlist = service.getWatchlistById(watchlistId)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(watchlist)
    }

    @PostMapping("/item/{mediaId}", produces = [APPLICATION_JSON_VALUE])
    fun addItem(@PathVariable mediaId: MediaId): BasicMediaItem {
        return service.addItemByMediaId(watchlistId, mediaId)
    }

    @DeleteMapping("/item/{mediaId}", produces = [APPLICATION_JSON_VALUE])
    fun deleteItem(@PathVariable mediaId: MediaId): BasicMediaItem {
        return service.removeItemByMediaId(watchlistId, mediaId)
    }

    @ExceptionHandler(
        MediaItemNotFoundException::class,
        WatchlistAlreadyContainsMediaItemException::class,
        WatchlistDoesNotContainMediaItemException::class
    )
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    fun handleUnprocessableEntity(e: Throwable) {
        log.error { "Error handling requested MediaItem because: [${e.message}]" }
    }
}
