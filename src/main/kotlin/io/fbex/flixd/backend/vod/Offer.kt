package io.fbex.flixd.backend.vod

data class Offer(
    val type: OfferType,
    val provider: Provider,
    val urls: OfferUrls
)

data class OfferUrls(
    val web: String?,
    val android: String?,
    val ios: String?
)

enum class OfferType(val value: String) {
    FLATRATE("flatrate"),
    BUY("buy"),
    RENT("rent")
}
