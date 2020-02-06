package io.fbex.flixd.backend.vod

import com.fasterxml.jackson.databind.JsonNode
import io.fbex.flixd.backend.vod.OfferType.FLATRATE
import io.fbex.flixd.backend.vod.Provider.Companion.forProviderId
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class VodFacade(
    private val justWatch: JustWatchService
) {

    suspend fun searchMovie(tmdbId: Int, title: String, releaseDate: LocalDate): VodInformation? {
        val result = justWatch.searchMovie(title = title, year = releaseDate.year)
        val items = result.get("items")?.toList() ?: emptyList()
        if (items.isEmpty()) return null

        val vodInformation = mapResult(items)
        val matchesQuery = vodInformation.matchesQuery(tmdbId = tmdbId, title = title, year = releaseDate.year)
        return if (matchesQuery) vodInformation else null
    }

    private fun mapResult(items: List<JsonNode>): VodInformation {
        return items.first().let { result ->
            VodInformation(
                justWatchId = result.get("id").asInt(),
                title = result.get("title").asText(),
                year = result.get("original_release_year").asInt(),
                tmdbId = result.findProviderId("tmdb:id"),
                tomatoId = result.findProviderId("tomato:id"),
                offers = result.findOffers()
            )
        }
    }

    private fun JsonNode.findProviderId(providerType: String): Int? =
        this.get("scoring")
            ?.toList()
            ?.find { it.get("provider_type").asText() == providerType }
            ?.get("value")
            ?.asInt()

    private fun JsonNode.findOffers(): List<Offer> =
        this.get("offers")
            ?.toList()
            ?.filter { it.get("monetization_type").asText() == FLATRATE.value }
            ?.distinctBy { it.get("provider_id") }
            ?.map {
                Offer(
                    type = FLATRATE,
                    provider = forProviderId(it.get("provider_id").asInt()),
                    urls = it.get("urls").let { urls ->
                        OfferUrls(
                            web = urls.get("standard_web")?.asText(),
                            android = urls.get("deeplink_android")?.asText(),
                            ios = urls.get("deeplink_ios")?.asText()
                        )
                    }
                )
            } ?: emptyList()
}
