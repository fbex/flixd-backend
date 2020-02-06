package io.fbex.flixd.backend.vod

val VOD_PULP_FICTION = VodInformation(
    justWatchId = 112130,
    title = "Pulp Fiction",
    year = 1994,
    tmdbId = 680,
    tomatoId = 13863,
    offers = listOf(
        Offer(
            type = OfferType.FLATRATE,
            provider = Provider.NETFLIX,
            urls = OfferUrls(
                web = "http://www.netflix.com/title/880640",
                android = "nflx://www.netflix.com/Browse?q=action%3Dplay%26source%3Dmerchweb%26target_url%3Dhttp%3A%2F%2Fmovi.es%2F7tEu",
                ios = "nflx://www.netflix.com/Browse?q=action%3Dview_details%26target_url%3Dhttp%253A%252F%252Fmovi.es%252F7tEu"
            )
        ),
        Offer(
            type = OfferType.FLATRATE,
            provider = Provider.AMAZON_PRIME,
            urls = OfferUrls(
                web = "https://www.amazon.de/gp/product/B00IGK7D06?camp=1638&creativeASIN=B00IGK7D06&ie=UTF8&linkCode=xm2&tag=movie0c6-21",
                android = "intent://watch.amazon.de/watch?asin=DE&contentType=MOVIE&territory=B00IGK7D06&ref_=atv_dp_pb_core#Intent;scheme=https;package=com.amazon.avod.thirdpartyclient;end",
                ios = "aiv-de://aiv/resume?_encoding=UTF8&asin=B00IGK7D06&time=0"
            )
        ),
        Offer(
            type = OfferType.FLATRATE,
            provider = Provider.SKY_GO,
            urls = OfferUrls(
                web = "https://ad.zanox.com/ppc/?41728307C1348667999&ulp=[[http://www.skygo.sky.de/film/thriller/pulp-fiction/asset/filmsection/122681.html]]",
                android = null,
                ios = null
            )
        ),
        Offer(
            type = OfferType.FLATRATE,
            provider = Provider.SKY_TICKET,
            urls = OfferUrls(
                web = "https://ad.zanox.com/ppc/?41728307C1348667999&ulp=[[http://skyticket.sky.de/film/thriller/pulp-fiction/asset/filmsection/122681.html]]",
                android = null,
                ios = null
            )
        )
    )
)
