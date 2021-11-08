package com.example.drewmovies.data

import android.os.Parcelable
import android.os.Parcel
import android.os.Parcelable.Creator
import java.util.*

//might not need Parcelable now, but will make it easier to include new features and speed up the
//process of transferring objects between activities and intents in the future if needed
data class Review (
    var reviewId: String,
    var reviewAuthor: String,
    var reviewContent: String,
    var rating: Number,
    var authorAvatarPath: String,
    var reviewDate: Calendar
)  {
    constructor() : this("", "", "", 0.0, "", Calendar.getInstance())
}