package com.example.drewmovies.data

import android.os.Parcelable
import android.os.Parcel
import android.os.Parcelable.Creator

//might not need Parcelable now, but will make it easier to include new features and speed up the
//process of transferring objects between activities and intents in the future if needed
data class Video (
    var videoId: Int,
    var videoKey: String,
    var videoSite: String,
    var videoType: String,
    var videoName: String,
)  {
    constructor() : this(0, "", "", "", "")
}