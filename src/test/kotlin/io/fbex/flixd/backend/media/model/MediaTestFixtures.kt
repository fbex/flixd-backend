package io.fbex.flixd.backend.media.model

import java.time.LocalDate

val MEDIA_ID_MOVIE_SHAWSHANK_REDEPMTION = MediaId("movie-278")
val MEDIA_ID_MOVIE_SILENCE_OF_THE_LAMBS = MediaId("movie-274")
val MEDIA_ID_TV_SCRUBS = MediaId("tv-4556")

val BASIC_MOVIE_SHAWSHANK_REDEMPTION = BasicMediaItem(
    mediaId = MEDIA_ID_MOVIE_SHAWSHANK_REDEPMTION,
    tmdbId = 278,
    imdbId = "tt0111161",
    type = MediaType.Movie,
    title = "Die Verurteilten",
    originalTitle = "The Shawshank Redemption",
    originalLanguage = "en",
    releaseDate = LocalDate.parse("1994-09-23"),
    voteAverage = 8.7,
    voteCount = 16098,
    popularity = 45.386,
    genreIds = listOf(18, 80),
    posterPath = "/78Pb6FMLMfpm1jUOKTniwREYgAN.jpg",
    backdropPath = "/avedvodAZUcwqevBfm8p4G2NziQ.jpg"
)

val SEARCH_MOVIE_SHAWSHANK_REDEMPTION = BASIC_MOVIE_SHAWSHANK_REDEMPTION.copy(imdbId = null)

val BASIC_MOVIE_SILENCE_OF_THE_LAMBS = BasicMediaItem(
    mediaId = MEDIA_ID_MOVIE_SILENCE_OF_THE_LAMBS,
    tmdbId = 274,
    type = MediaType.Movie,
    title = "Das Schweigen der Lämmer",
    originalTitle = "The Silence of the Lambs",
    imdbId = "tt0102926",
    originalLanguage = "en",
    releaseDate = LocalDate.parse("1991-02-01"),
    voteAverage = 8.3,
    voteCount = 10146,
    popularity = 6.51,
    genreIds = listOf(80, 18, 53, 27),
    posterPath = "/rln3cSeqRusXZLjfWKdXh1c7C06.jpg",
    backdropPath = "/pI9B4wppGflpsOD2T6rxgC5Clmz.jpg"
)

val BASIC_TV_SCRUBS = BasicMediaItem(
    mediaId = MEDIA_ID_TV_SCRUBS,
    tmdbId = 4556,
    imdbId = null,
    type = MediaType.TvShow,
    title = "Scrubs - Die Anfänger",
    originalTitle = "Scrubs",
    originalLanguage = "en",
    releaseDate = LocalDate.parse("2001-10-02"),
    voteAverage = 7.9,
    voteCount = 768,
    popularity = 40.779,
    genreIds = listOf(35),
    posterPath = "/u1z05trCA7AuSuDhi365grwdos1.jpg",
    backdropPath = "/sVaCswyCaBdCMIfClV1caOBCoKT.jpg"
)

val MOVIE_SHAWSHANK_REDEMPTION = Movie(
    title = "Die Verurteilten",
    originalTitle = "The Shawshank Redemption",
    releaseDate = LocalDate.parse("1994-09-23"),
    mediaId = MEDIA_ID_MOVIE_SHAWSHANK_REDEPMTION,
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

val MOVIE_SILENCE_OF_THE_LAMBS = Movie(
    title = "Das Schweigen der Lämmer",
    originalTitle = "The Silence of the Lambs",
    releaseDate = LocalDate.parse("1991-02-01"),
    mediaId = MEDIA_ID_MOVIE_SILENCE_OF_THE_LAMBS,
    tmdbId = 274,
    imdbId = "tt0102926",
    originalLanguage = "en",
    voteAverage = 8.3,
    voteCount = 10146,
    popularity = 6.51,
    genres = listOf(
        Genre(80, "Krimi"),
        Genre(18, "Drama"),
        Genre(53, "Thriller"),
        Genre(27, "Horro")
    ),
    posterPath = "/rln3cSeqRusXZLjfWKdXh1c7C06.jpg",
    backdropPath = "/pI9B4wppGflpsOD2T6rxgC5Clmz.jpg"
)

val TV_SCRUBS = TvShow(
    name = "Scrubs - Die Anfänger",
    originalName = "Scrubs",
    firstAirDate = LocalDate.parse("2001-10-02"),
    lastAirDate = LocalDate.parse("2010-03-17"),
    mediaId = MEDIA_ID_TV_SCRUBS,
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
