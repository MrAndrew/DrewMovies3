package com.example.drewmovies.utils

import android.util.Log
import com.example.drewmovies.data.Movie
import com.example.drewmovies.data.MovieDetails
import com.example.drewmovies.data.Review
import com.example.drewmovies.data.Video
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

object JsonParsers {

    //JSON Movie Keys
    private const val RESULTS_ARRAY_KEY = "results"
    private const val MOVIE_ID_KEY = "id"
    private const val MOVIE_TITLE_KEY = "title"
    private const val MOVIE_IMAGE_PATH_KEY = "backdrop_path"
    private const val DESCRIPTION_KEY = "overview"
    private const val RELEASE_DATE_KEY = "release_date"
    private const val USER_RATING_KEY = "vote_average"

    //JSON Review Keys
    private const val REVIEW_KEY_AUTHOR_DETAILS = "author_details"
    private const val REVIEW_KEY_AUTHOR_USERNAME = "username"
    private const val REVIEW_KEY_AVATAR_PATH = "avatar_path"
    private const val REVIEW_KEY_CONTENT = "content"
    private const val REVIEW_KEY_ID = "id"
    private const val REVIEW_KEY_URL = "url"
    private const val REVIEW_KEY_RATING = "rating"
    private const val REVIEW_KEY_DATE = "created_at"

    //JSON Video Keys
    private const val VIDEO_KEY_ID = "id"
    private const val VIDEO_KEY_KEY = "key"
    private const val VIDEO_KEY_SITE = "site"
    private const val VIDEO_KEY_TYPE = "type"
    private const val VIDEO_KEY_NAME = "name"

    //JSON Detail Keys
    private const val LANGUAGES_KEY = "spoken_languages"
    private const val ENGLISH_NAME_KEY = "english_name"
    private const val GENRES_KEY = "genres"
    private const val GENRE_NAME_KEY = "name"
    private const val POPULARITY_KEY = "popularity"
    private const val PRODUCTION_COMPANIES = "production_companies"
    private const val PRODUCTION_COMPANY_NAME = "name"
    private const val PRODUCTION_COUNTRIES = "production_countries"
    private const val PRODUCTION_COMPANY_LOCATION = "name"
    private const val RUNTIME_KEY = "runtime"
    private const val USER_RATINGS_NUMOF_KEY = "vote_count"
    private const val USER_RATINGS_AVERAGE_KEY = "vote_average"
    private const val REVENUE_KEY = "revenue"

    fun parseMoviesJson(jsonString: String?): ArrayList<Movie> {

        //create new arraylist of movie objects to return
        val moviesList: ArrayList<Movie> = ArrayList<Movie>()

        //get and set movies from results object array in the json string
        try {
            val resultsObj = JSONObject(jsonString)

            val moviesJsonArray = resultsObj.getJSONArray(RESULTS_ARRAY_KEY)

            //loop through all the objects in the results array that should be returned in the JSON
            for (i in 0 until moviesJsonArray.length()) {
                //create new movie object
                val movie = Movie()
                val movieJsonObj = moviesJsonArray.getJSONObject(i)
                //                Log.v(TAG, "movieJsonArray[" + i + "] : " + movieJsonObj);
                //set values to object within the array
                movie.id = (movieJsonObj.getInt(MOVIE_ID_KEY))
                movie.movieTitle = (movieJsonObj.getString(MOVIE_TITLE_KEY))
                movie.imageUrlPath = (movieJsonObj.getString(MOVIE_IMAGE_PATH_KEY))
                movie.about = (movieJsonObj.getString(DESCRIPTION_KEY))

                val format = SimpleDateFormat("yyyy-MM-dd")
                val date: Date? = format.parse((movieJsonObj.getString(RELEASE_DATE_KEY)))
                val cal = Calendar.getInstance()
                cal.time = date ?: Date()
                movie.releaseDate = cal

                movie.userRating = (movieJsonObj.getDouble(USER_RATING_KEY))
                //set the movie obj to the corresponding position in the movie obj array
                moviesList.add(movie)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return moviesList
    }

    fun parseVideosJson(jsonString: String?): ArrayList<Video> {
        //create new arraylist of video objects to return
        val videosList: ArrayList<Video> = ArrayList<Video>()
        //get and set videos from results object array in the json string
        try {
            val resultsObj = JSONObject(jsonString)
            val videosJsonArray = resultsObj.getJSONArray(RESULTS_ARRAY_KEY)
            //loop through all the objects in the results array that should be returned in the JSON
            for (i in 0 until videosJsonArray.length()) {
                //create new video object
                val video = Video()
                val videoJsonObj = videosJsonArray.getJSONObject(i)
                //set values to object within the array
                video.videoId = (videoJsonObj.getInt(VIDEO_KEY_ID))
                video.videoKey = (videoJsonObj.getString(VIDEO_KEY_KEY))
                video.videoSite = (videoJsonObj.getString(VIDEO_KEY_SITE))
                video.videoType = (videoJsonObj.getString(VIDEO_KEY_TYPE))
                video.videoName = (videoJsonObj.getString(VIDEO_KEY_NAME))
                //set the movie obj to the corresponding position in the movie obj array
                videosList.add(video)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return videosList
    }

    fun parseReviewsJson(jsonString: String?): ArrayList<Review> {
        Log.d("DEBUG", "parseReviewsJson() jsonString: $jsonString")
        //create new arraylist of video objects to return
        val reviewsList: ArrayList<Review> = ArrayList<Review>()
        //get and set videos from results object array in the json string
//        try {
            val resultsObj = JSONObject(jsonString)
            val reviewsJsonArray = resultsObj.getJSONArray(RESULTS_ARRAY_KEY)
            //loop through all the objects in the results array that should be returned in the JSON
            for (i in 0 until reviewsJsonArray.length()) {
                //create new video object
                val review = Review()
                val reviewJsonObj = reviewsJsonArray.getJSONObject(i)
                Log.d("DEBUG", "parseReviewsJson() reviewJsonObj: $reviewJsonObj")
                //set values to object within the array
                review.reviewContent = (reviewJsonObj.getString(REVIEW_KEY_CONTENT))
                review.reviewId = (reviewJsonObj.getString(REVIEW_KEY_ID))
                val authorDetails = (reviewJsonObj.getJSONObject(REVIEW_KEY_AUTHOR_DETAILS))
                review.reviewAuthor = (authorDetails.getString(REVIEW_KEY_AUTHOR_USERNAME))
                var rating = (authorDetails.get(REVIEW_KEY_RATING))
                Log.d("DEBUG", "parseReviewsJson() rating before: $rating")
                Log.d("DEBUG", "parseReviewsJson() rating before: ${rating is Number}")
//                rating = rating ?: 0
//                rating?.let { rating = 0 }
                if (rating !is Number) { // same as !(obj is String)
                    rating = 0
                }
                Log.d("DEBUG", "parseReviewsJson() rating after: $rating")
                review.rating = rating as Number
                review.authorAvatarPath = (authorDetails.getString(REVIEW_KEY_AVATAR_PATH))
                val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                val date: Date? = format.parse((reviewJsonObj.getString(REVIEW_KEY_DATE)))
                val cal = Calendar.getInstance()
                cal.time = date ?: Date()
                review.reviewDate = cal
                //set the movie obj to the corresponding position in the movie obj array
                reviewsList.add(review)
            }
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
        Log.d("DEBUG", "parseReviewsJson() reviewsList: $reviewsList")
        return reviewsList
    }

    fun parseMovieDetailsJson(jsonString: String?) : MovieDetails {
        Log.d("DEBUG", "JsonParsers jsonString: ${jsonString}")
        //create new arraylist of movie objects to return
        val movieDetails = MovieDetails()
        //get and set movies from results object array in the json string
        try {
            val resultsObj = JSONObject(jsonString)
            movieDetails.id = (resultsObj.getInt(MOVIE_ID_KEY))
            movieDetails.title = (resultsObj.getString(MOVIE_TITLE_KEY))
            val spokenLanguages = resultsObj.getJSONArray(LANGUAGES_KEY)
            for (i in 0 until spokenLanguages.length()) {
                movieDetails.languages = movieDetails.languages.plus((spokenLanguages.get(i) as JSONObject).getString(ENGLISH_NAME_KEY))
            }
            val genres = resultsObj.getJSONArray(GENRES_KEY)
            for (i in 0 until genres.length()) {
                movieDetails.genres = movieDetails.genres.plus((genres.get(i) as JSONObject).getString(GENRE_NAME_KEY))
            }
            movieDetails.popularity = (resultsObj.getDouble(POPULARITY_KEY))
            movieDetails.imageUrlPath = (resultsObj.getString(MOVIE_IMAGE_PATH_KEY))
            movieDetails.overview = (resultsObj.getString(DESCRIPTION_KEY))
            val productionCompanies = resultsObj.getJSONArray(PRODUCTION_COMPANIES)
            for (i in 0 until productionCompanies.length()) {
                movieDetails.productionCompanyNames = movieDetails.productionCompanyNames.plus((productionCompanies.get(i) as JSONObject).getString(PRODUCTION_COMPANY_NAME))
            }
            val productionCountries = resultsObj.getJSONArray(PRODUCTION_COUNTRIES)
            for (i in 0 until productionCountries.length()) {
                movieDetails.productionCompanyLocations = movieDetails.productionCompanyLocations.plus((productionCountries.get(i) as JSONObject).getString(PRODUCTION_COMPANY_LOCATION))
            }
            val cal = Calendar.getInstance()
            val sdf = SimpleDateFormat("yyyy-MM-DD", Locale.ENGLISH)
            cal.time = sdf.parse(resultsObj.getString(RELEASE_DATE_KEY))!!
            movieDetails.releaseDate = cal
            movieDetails.runtime = (resultsObj.getInt(RUNTIME_KEY))
            movieDetails.numOfVotes = (resultsObj.getInt(USER_RATINGS_NUMOF_KEY))
            movieDetails.voteAvg = (resultsObj.getInt(USER_RATINGS_AVERAGE_KEY))

            movieDetails.revenue = (resultsObj.getInt(REVENUE_KEY))

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Log.d("DEBUG", "JsonParsers movieDetails: ${movieDetails}")
        return movieDetails
    }
}