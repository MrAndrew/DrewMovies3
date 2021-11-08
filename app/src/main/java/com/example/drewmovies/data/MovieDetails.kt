package com.example.drewmovies.data

import java.util.*

data class MovieDetails (
    var id: Int,
    var title: String,
    var languages: List<String>,
    var genres: List<String>,
    var popularity: Number,
    var imageUrlPath: String,
    var overview: String,
    var productionCompanyNames: List<String>,
    var productionCompanyLocations: List<String>,
    var releaseDate: Calendar,
    var runtime: Int,
    var numOfVotes: Number,
    var voteAvg: Number,
    var revenue: Number,
)  {
    constructor() : this(1, "", listOf(), listOf(), 0, "", "", listOf(), listOf(), Calendar.getInstance(),0, 0, 0, 0)
}