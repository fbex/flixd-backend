package io.fbex.flixd.backend.media.model

import java.time.LocalDate

val SEARCH_ITEM_SHAWSHANK_REDEMPTION = MediaSearchItem(
    tmdbId = 278,
    type = MediaSearchItem.Type.Movie,
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

val SEARCH_ITEM_SCRUBS = MediaSearchItem(
    tmdbId = 4556,
    type = MediaSearchItem.Type.TvShow,
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

val MOVIE_SHAWSHANK_REDEMPTION = Movie(
    title = "Die Verurteilten",
    originalTitle = "The Shawshank Redemption",
    releaseDate = LocalDate.parse("1994-09-23"),
    tmdbId = 278,
    imdbId = "tt0111161",
    originalLanguage = "en",
    voteAverage = 8.7,
    voteCount = 16098,
    popularity = 45.386,
    genres = listOf(
        Genre(18, "Drama"),
        Genre(80, "Krimi")
    ),
    posterPath = "/78Pb6FMLMfpm1jUOKTniwREYgAN.jpg",
    backdropPath = "/avedvodAZUcwqevBfm8p4G2NziQ.jpg"
)

val TV_SCRUBS = TvShow(
    name = "Scrubs - Die Anfänger",
    originalName = "Scrubs",
    firstAirDate = LocalDate.parse("2001-10-02"),
    lastAirDate = LocalDate.parse("2010-03-17"),
    tmdbId = 4556,
    imdbId = null,
    originalLanguage = "en",
    voteAverage = 7.9,
    voteCount = 768,
    popularity = 40.779,
    genres = listOf(Genre(35, "Komödie")),
    posterPath = "/u1z05trCA7AuSuDhi365grwdos1.jpg",
    backdropPath = "/sVaCswyCaBdCMIfClV1caOBCoKT.jpg"
)
