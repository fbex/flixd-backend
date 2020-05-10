package io.fbex.flixd.backend.tmdb

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "tmdb")
data class TmdbProperties(
    val url: String,
    val apiKey: String
)
