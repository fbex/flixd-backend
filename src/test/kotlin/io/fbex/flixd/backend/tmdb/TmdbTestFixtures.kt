package io.fbex.flixd.backend.tmdb

import java.time.LocalDate

val MOVIE_PULP_FICTION_SEARCH = TmdbMovieResult(
    id = 680,
    vote_count = 17326,
    video = false,
    vote_average = 8.5,
    title = "Pulp Fiction",
    popularity = 28.514,
    poster_path = "/d5iIlFn5s0ImszYzBPb8JPIfbXD.jpg",
    original_title = "Pulp Fiction",
    original_language = "en",
    genre_ids = listOf(80, 53),
    backdrop_path = "/suaEOtk1N1sgg2MTM7oZd2cfVp3.jpg",
    adult = false,
    overview = "A burger-loving hit man, his philosophical partner, a drug-addled gangster's moll and a washed-up boxer converge in this sprawling, comedic crime caper. Their adventures unfurl in three stories that ingeniously trip back and forth in time.",
    release_date = LocalDate.of(1994, 9, 10)
)

val MOVIE_PULP_FICTION_DETAILS = TmdbMovieDetails(
    adult = false,
    backdrop_path = "/suaEOtk1N1sgg2MTM7oZd2cfVp3.jpg",
    belongs_to_collection = null,
    budget = 8000000,
    genres = listOf(
        Genre(id = 53, name = "Thriller"),
        Genre(id = 80, name = "Crime")
    ),
    homepage = "",
    id = 680,
    imdb_id = "tt0110912",
    original_language = "en",
    original_title = "Pulp Fiction",
    overview = "A burger-loving hit man, his philosophical partner, a drug-addled gangster's moll and a washed-up boxer converge in this sprawling, comedic crime caper. Their adventures unfurl in three stories that ingeniously trip back and forth in time.",
    popularity = 28.514,
    poster_path = "/d5iIlFn5s0ImszYzBPb8JPIfbXD.jpg",
    production_companies = listOf(
        ProductionCompany(
            id = 14,
            logo_path = "/m6AHu84oZQxvq7n1rsvMNJIAsMu.png",
            name = "Miramax",
            origin_country = "US"
        ),
        ProductionCompany(
            id = 59,
            logo_path = "/yH7OMeSxhfP0AVM6iT0rsF3F4ZC.png",
            name = "A Band Apart",
            origin_country = "US"
        ),
        ProductionCompany(
            id = 216,
            logo_path = null,
            name = "Jersey Films",
            origin_country = ""
        )
    ),
    production_countries = listOf(
        ProductionCountry(
            iso_3166_1 = "US",
            name = "United States of America"
        )
    ),
    release_date = LocalDate.of(1994, 9, 10),
    revenue = 214179088,
    runtime = 154,
    spoken_languages = listOf(
        Language(
            iso_639_1 = "en",
            name = "English"
        ),
        Language(
            iso_639_1 = "es",
            name = "Español"
        ),
        Language(
            iso_639_1 = "fr",
            name = "Français"
        )
    ),
    status = "Released",
    tagline = "Just because you are a character doesn't mean you have character.",
    title = "Pulp Fiction",
    video = false,
    vote_average = 8.5,
    vote_count = 17331
)

val MOVIE_GOODFELLAS_DETAILS = TmdbMovieDetails(
    adult = false,
    backdrop_path = "/sw7mordbZxgITU877yTpZCud90M.jpg",
    belongs_to_collection = null,
    budget = 25000000,
    genres = listOf(
        Genre(id = 18, name = "Drama"),
        Genre(id = 80, name = "Crime")
    ),
    homepage = "http://www.warnerbros.com/goodfellas",
    id = 769,
    imdb_id = "tt0099685",
    original_language = "en",
    original_title = "GoodFellas",
    overview = "The true story of Henry Hill, a half-Irish, half-Sicilian Brooklyn kid who is adopted by neighbourhood gangsters at an early age and climbs the ranks of a Mafia family under the guidance of Jimmy Conway.",
    popularity = 22.212,
    poster_path = "/pwpGfTImTGifEGgLb3s6LRPd4I6.jpg",
    production_companies = listOf(
        ProductionCompany(
            id = 8880,
            logo_path = "/fE7LBw7Jz8R29EABFGCvWNriZxN.png",
            name = "Winkler Films",
            origin_country = "US"
        )
    ),
    production_countries = listOf(
        ProductionCountry(
            iso_3166_1 = "US",
            name = "United States of America"
        )
    ),
    release_date = LocalDate.of(1990, 9, 12),
    revenue = 46836394,
    runtime = 145,
    spoken_languages = listOf(
        Language(
            iso_639_1 = "it",
            name = "Italiano"
        ),
        Language(
            iso_639_1 = "en",
            name = "English"
        )
    ),
    status = "Released",
    tagline = "Three Decades of Life in the Mafia.",
    title = "GoodFellas",
    video = false,
    vote_average = 8.4,
    vote_count = 6609
)

val MOVIE_THE_GODFATHER_DETAILS = TmdbMovieDetails(
    adult = false,
    backdrop_path = "/6xKCYgH16UuwEGAyroLU6p8HLIn.jpg",
    belongs_to_collection = Collection(
        id = 230,
        name = "The Godfather Collection",
        poster_path = "/9Baumh5J9N1nJUYzNkm0xsgjpwY.jpg",
        backdrop_path = "/3WZTxpgscsmoUk81TuECXdFOD0R.jpg"
    ),
    budget = 6000000,
    genres = listOf(
        Genre(id = 18, name = "Drama"),
        Genre(id = 80, name = "Crime")
    ),
    homepage = "http://www.thegodfather.com/",
    id = 238,
    imdb_id = "tt0068646",
    original_language = "en",
    original_title = "The Godfather",
    overview = "Spanning the years 1945 to 1955, a chronicle of the fictional Italian-American Corleone crime family. When organized crime family patriarch, Vito Corleone barely survives an attempt on his life, his youngest son, Michael steps in to take care of the would-be killers, launching a campaign of bloody revenge.",
    popularity = 25.377,
    poster_path = "/rPdtLWNsZmAtoZl9PK7S2wE3qiS.jpg",
    production_companies = listOf(
        ProductionCompany(
            id = 4,
            logo_path = "/fycMZt242LVjagMByZOLUGbCvv3.png",
            name = "Paramount",
            origin_country = "US"
        ),
        ProductionCompany(
            id = 10211,
            logo_path = null,
            name = "Alfran Productions",
            origin_country = "US"
        )
    ),
    production_countries = listOf(
        ProductionCountry(
            iso_3166_1 = "US",
            name = "United States of America"
        )
    ),
    release_date = LocalDate.of(1972, 3, 14),
    revenue = 245066411,
    runtime = 175,
    spoken_languages = listOf(
        Language(
            iso_639_1 = "en",
            name = "English"
        ),
        Language(
            iso_639_1 = "it",
            name = "Italiano"
        ),
        Language(
            iso_639_1 = "la",
            name = "Latin"
        )
    ),
    status = "Released",
    tagline = "An offer you can't refuse.",
    title = "The Godfather",
    video = false,
    vote_average = 8.6,
    vote_count = 11358
)
