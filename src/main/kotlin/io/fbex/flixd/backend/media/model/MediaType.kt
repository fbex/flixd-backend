package io.fbex.flixd.backend.media.model

enum class MediaType(val code: String) {
    Movie("movie"), TvShow("tv");

    companion object {
        fun fromCode(code: String): MediaType = when (code) {
            Movie.code -> Movie
            TvShow.code -> TvShow
            else -> throw IllegalArgumentException("Code [$code] does not match a MediaType.")
        }
    }
}
