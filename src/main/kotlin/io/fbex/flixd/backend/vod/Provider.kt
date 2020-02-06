package io.fbex.flixd.backend.vod

enum class Provider {
    NETFLIX,
    AMAZON_PRIME,
    SKY_GO,
    SKY_TICKET,
    UNKNOWN;

    companion object {
        fun forProviderId(id: Int): Provider = when (id) {
            8 -> NETFLIX
            9 -> AMAZON_PRIME
            29 -> SKY_GO
            30 -> SKY_TICKET
            else -> UNKNOWN
        }
    }
}
