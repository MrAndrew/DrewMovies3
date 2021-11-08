package com.example.drewmovies.data

import android.os.Parcelable
import android.os.Parcel
import android.os.Parcelable.Creator
import java.util.*

data class Movie(

    var id: Int,
    var movieTitle: String,
    var imageUrlPath: String,
    var about: String,
    var releaseDate: Calendar,
    var userRating: Double

)  {
    constructor() : this(1, "", "", "", Calendar.getInstance(), 0.0)
}