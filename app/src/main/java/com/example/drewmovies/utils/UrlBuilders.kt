package com.example.drewmovies.utils

import android.net.Uri
import java.net.MalformedURLException
import java.net.URL

object UrlBuilders {
    //    private static final String TAG = BuildUrlUtils.class.getSimpleName();
    //base urls for different types of requests
    private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/"
    private const val MOVIE_LIST_POP_BASE_URL = "https://api.themoviedb.org/3/movie/popular"
    private const val MOVIE_LIST_RATE_BASE_URL = "https://api.themoviedb.org/3/movie/top_rated"
    private const val MOVIE_VIDEOS_BASE_URL = "https://api.themoviedb.org/3/movie/"
    private const val MOVIE_REVIEWS_BASE_URL = "https://api.themoviedb.org/3/movie/"
    private const val MOVIE_DETAILS_BASE_URL = "https://api.themoviedb.org/3/movie/"

    //current size seems to be the best size for now, might change to user preference value in phase 2
    private const val PARAM_POSTER_SIZE_SMALL = "w500"
    private const val PARAM_POSTER_SIZE_MED = "w780"
    private const val PARAM_POSTER_SIZE_LARGE = "original"

    //lang and page num strings
    // ex: &language=en-US&page=1
    // might be better to change to selectable strings from value resource folder, especially for
    //language portability for different users
    private const val PARAM_LANG = "language"
    private const val lang_en_us = "en-US"
    private const val PARAM_PAGE = "page"
    private const val page_num = "1"

    private const val api_key = APIKeys.themoviedb_api_key
    private const val PARAM_API_KEY = "api_key"

    //video and reviews requests
    private const val video_path = "videos"
    private const val review_path = "reviews"

    fun buildMovieDetailsRequestUrl(id: String?): URL? {
        val builtUri = Uri.parse(MOVIE_DETAILS_BASE_URL).buildUpon()
            .appendPath(id)
            .appendQueryParameter(PARAM_API_KEY, api_key)
            .appendQueryParameter(PARAM_LANG, lang_en_us)
            .build()
        var url: URL? = null
        try {
            url = URL(builtUri.toString())
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return url
    }

    /**
     * Video Links DB url request example:
     * https://api.themoviedb.org/3/movie/<<MOVIE_ID>>/videos?api_key=<<API_KEY>>&language=en-US
     *
     * json returned contains array of objects called "results" which may be different lengths
     * (like 5 in the array for 5 different videos)
     * results.key is the youtube url ending to add like:
     * https://www.youtube.com/watch?v=<<results.key>>
    </results.key></API_KEY></MOVIE_ID> */
    fun buildMovieVideosRequestUrl(id: String?): URL? {
        val builtUri = Uri.parse(MOVIE_VIDEOS_BASE_URL).buildUpon()
            .appendPath(id)
            .appendPath(video_path)
            .appendQueryParameter(PARAM_API_KEY, api_key)
            .appendQueryParameter(PARAM_LANG, lang_en_us)
            .build()
        //        Log.d(TAG, "video request url: " + builtUri.toString());
        var url: URL? = null
        try {
            url = URL(builtUri.toString())
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return url
    }

    /**
     * Reviews DB url request example:
     * https://api.themoviedb.org/3/movie/<<MOVIE_ID>>/reviews?api_key=<<API_KEY>>&language=en-US&page=1
     *
     * json returned contains array of objects called "results" which may be different lengths
     * (like 11 in the array for 11 different reviews all on themoviedb.org)
     * results.author is the username of the reviewer (String)
     * results.content is the actual text of the review (String)
     *
    </API_KEY></MOVIE_ID> */
    fun buildMovieReviewsRequestUrl(id: String?): URL? {
        val builtUri = Uri.parse(MOVIE_REVIEWS_BASE_URL).buildUpon()
            .appendPath(id)
            .appendPath(review_path)
            .appendQueryParameter(PARAM_API_KEY, api_key)
            .appendQueryParameter(PARAM_LANG, lang_en_us)
            .build()
        //        Log.d(TAG, "reviews request url: " + builtUri.toString());
        var url: URL? = null
        try {
            url = URL(builtUri.toString())
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return url
    }

    /**
     * Builds the URL to request list of movies in JSON format for popularity
     * ex: https://api.themoviedb.org/3/movie/popular?api_key=<<api_key>>&language=en-US&page=1
    </api_key> */
    fun buildMovieListPopRequestUrl(): URL? {
        val builtUri = Uri.parse(MOVIE_LIST_POP_BASE_URL).buildUpon()
            .appendQueryParameter(PARAM_API_KEY, api_key)
            .appendQueryParameter(PARAM_LANG, lang_en_us)
            .appendQueryParameter(PARAM_PAGE, page_num)
            .build()
        //        Log.d(TAG, "movie list url: " + builtUri.toString());
        var url: URL? = null
        try {
            url = URL(builtUri.toString())
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return url
    }

    /**
     * Builds the URL to request list of movies in JSON format based on rating
     * ex: https://api.themoviedb.org/3/movie/top_rated?api_key=<APIKEY>&language=en-US&page=1
    </APIKEY> */
    fun buildMovieListRatedRequestUrl(): URL? {
        val builtUri = Uri.parse(MOVIE_LIST_RATE_BASE_URL).buildUpon()
            .appendQueryParameter(PARAM_API_KEY, api_key)
            .appendQueryParameter(PARAM_LANG, lang_en_us)
            .appendQueryParameter(PARAM_PAGE, page_num)
            .build()
        //        Log.d(TAG, "movie list url: " + builtUri.toString());
        var url: URL? = null
        try {
            url = URL(builtUri.toString())
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return url
    }

    /**
     * Builds the URL used to query themoviedb.org for a poster picture
     * ex: http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg
     *
     * returns string because it will be used by picasso
     */
    fun buildImageRequestUrl(posterPath: String, size: String = "default"): String {
        // removes the first annoying "\" included in the api json response
        val sizePath = when (size) {
            "large" -> PARAM_POSTER_SIZE_LARGE
            "medium" -> PARAM_POSTER_SIZE_MED
            "default" -> PARAM_POSTER_SIZE_SMALL
            else -> PARAM_POSTER_SIZE_SMALL
        }
        val imagePath = posterPath.substring(1)
        val builtUri = Uri.parse(IMAGE_BASE_URL).buildUpon()
            .appendPath(sizePath)
            .appendPath(imagePath)
            .build()
        //        Log.d(TAG, "image request url: " + builtUri.toString());

        //changed to reduce redundancy caught in lint
        return builtUri.toString()
    }

    /**
     * Builds the URL used to query themoviedb.org for a poster picture
     * ex: http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg
     *
     * returns string because it will be used by picasso
     */
    fun buildUserAvatarImageRequestUrl(avatarPath: String, size: String = "default"): String {
        // removes the first annoying "\" included in the api json response
        val sizePath = when (size) {
            "large" -> PARAM_POSTER_SIZE_LARGE
            "medium" -> PARAM_POSTER_SIZE_MED
            "default" -> PARAM_POSTER_SIZE_SMALL
            else -> PARAM_POSTER_SIZE_SMALL
        }
        val imagePath = avatarPath.substring(1)
        val builtUri = Uri.parse(IMAGE_BASE_URL).buildUpon()
            .appendPath(sizePath)
            .appendPath(imagePath)
            .build()
        //        Log.d(TAG, "image request url: " + builtUri.toString());

        //changed to reduce redundancy caught in lint
        return builtUri.toString()
    }
}