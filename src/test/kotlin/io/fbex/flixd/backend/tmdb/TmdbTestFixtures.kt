package io.fbex.flixd.backend.tmdb

import java.time.LocalDate

val MOVIE_SILENCE_OF_THE_LAMBS = TmdbMovieResult(
    id = 274,
    title = "Das Schweigen der Lämmer",
    original_title = "The Silence of the Lambs",
    original_language = "en",
    vote_average = 8.3,
    vote_count = 10146,
    video = false,
    popularity = 6.51,
    poster_path = "/rln3cSeqRusXZLjfWKdXh1c7C06.jpg",
    genre_ids = listOf(80, 18, 27, 53),
    backdrop_path = "/pI9B4wppGflpsOD2T6rxgC5Clmz.jpg",
    adult = false,
    overview = "Clarice Starling, Auszubildende beim FBI, bekommt von einem ihrer Lehrer den Auftrag, sich im Gefängnis mit Dr. Hannibal Lecter, einem Massenmörder und Psychiater, zu befassen. Durch ihre Unbefangenheit hoffen sie, daß Lecter ihnen bei der Suche nach einem anderen Serienkiller hilft, aber es ist nicht leicht, an Lecter heranzukommen.",
    release_date = LocalDate.parse("1991-02-01")
)
