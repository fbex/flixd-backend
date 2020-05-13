package io.fbex.flixd.backend.media.tmdb

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "tmdb")
internal data class TmdbProperties(
    val url: String,
    val apiKey: String,
    val language: String
)
