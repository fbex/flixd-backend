package io.fbex.flixd.backend.vod

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "justwatch")
data class JustWatchProperties(
    val url: String
)
