package io.fbex.flixd.backend.tmdb

import java.time.LocalDate

val MOVIE_SHAWSHANK_REDEMPTION = MediaItem(
    tmdbId = 278,
    type = MediaItem.Type.Movie,
    title = "Die Verurteilten",
    originalTitle = "The Shawshank Redemption",
    originalLanguage = "en",
    releaseDate = LocalDate.parse("1994-09-23"),
    voteAverage = 8.7,
    voteCount = 16098,
    popularity = 45.386,
    genreIds = listOf(80, 18),
    posterPath = "/78Pb6FMLMfpm1jUOKTniwREYgAN.jpg",
    backdropPath = "/avedvodAZUcwqevBfm8p4G2NziQ.jpg"
)

val TV_SCRUBS = MediaItem(
    tmdbId = 4556,
    type = MediaItem.Type.TvShow,
    title = "Scrubs - Die Anfänger",
    originalTitle = "Scrubs",
    originalLanguage = "en",
    releaseDate = LocalDate.parse("2001-10-02"),
    voteAverage = 7.9,
    voteCount = 768,
    popularity = 42.175,
    genreIds = listOf(35),
    posterPath = "/u1z05trCA7AuSuDhi365grwdos1.jpg",
    backdropPath = "/sVaCswyCaBdCMIfClV1caOBCoKT.jpg"
)
