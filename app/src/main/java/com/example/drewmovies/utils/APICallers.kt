package com.example.drewmovies.utils

import android.util.Log
import com.example.drewmovies.data.Movie
import com.example.drewmovies.data.MovieDetails
import com.example.drewmovies.data.Review
import com.example.drewmovies.data.Video
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList

object APICallers {

    fun loadMoviesJsonFromUrl(inUrl: String): ArrayList<Movie> {
        Log.d("DEBUG", "url: $inUrl")
        var dlJsonString = ""
        try {
            val url = URL(inUrl)
            val `in` = BufferedReader(
                InputStreamReader(url.openStream()) //requires internet permission
            )
            Log.d("DEBUG", "after val in")
            val responseStrBuilder = StringBuilder()
            var inputLine: String?
            while (`in`.readLine().also { inputLine = it } != null) responseStrBuilder.append(
                inputLine
            )
            Log.d("DEBUG", "after while")
            `in`.close()
            Log.d("DEBUG", "after in close")
            dlJsonString = responseStrBuilder.toString()
            Log.d("DEBUG", "dlJsonString in try: $dlJsonString")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.d("DEBUG", "movies json: $dlJsonString")
        return JsonParsers.parseMoviesJson(dlJsonString)
    }

    public fun loadVideos(url: URL): ArrayList<Video> {
        var dlJsonString: String? = null
        val urlConnection = url.openConnection() as HttpURLConnection
        try {
            val `in` = urlConnection.inputStream
            val streamReader = BufferedReader(InputStreamReader(`in`, "UTF-8"))
            val responseStrBuilder = StringBuilder()
            var inputStr: String?
            while (streamReader.readLine()
                    .also { inputStr = it } != null
            ) responseStrBuilder.append(inputStr)
            dlJsonString = responseStrBuilder.toString()

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            urlConnection.disconnect()
        }
        return JsonParsers.parseVideosJson(dlJsonString)
    }

    fun loadReviews(url: URL): ArrayList<Review> {
        Log.d("DEBUG", "loadReviews() url: $url")
        var dlJsonString: String? = null
        val urlConnection = url.openConnection() as HttpURLConnection
        try {
            val `in` = urlConnection.inputStream
            val streamReader = BufferedReader(InputStreamReader(`in`, "UTF-8"))
            val responseStrBuilder = StringBuilder()
            var inputStr: String?
            while (streamReader.readLine()
                    .also { inputStr = it } != null
            ) responseStrBuilder.append(inputStr)
            dlJsonString = responseStrBuilder.toString()

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            urlConnection.disconnect()
        }
        Log.d("DEBUG", "loadReviews() dlJsonString: $dlJsonString")
        return JsonParsers.parseReviewsJson(dlJsonString)
    }

    fun loadMovieDetailsFromUrl(inUrl: URL): MovieDetails {
        Log.d("DEBUG", "APICallers url: $inUrl")
        var dlJsonString = ""
        try {
            val `in` = BufferedReader(
                InputStreamReader(inUrl.openStream()) //requires internet permission
            )
            Log.d("DEBUG", "APICallers after val in")
            val responseStrBuilder = StringBuilder()
            var inputLine: String?
            while (`in`.readLine().also { inputLine = it } != null) responseStrBuilder.append(
                inputLine
            )
            Log.d("DEBUG", "APICallers after while")
            `in`.close()
            Log.d("DEBUG", "APICallers after in close")
            dlJsonString = responseStrBuilder.toString()
            Log.d("DEBUG", "APICallers dlJsonString in try: $dlJsonString")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.d("DEBUG", "APICallers movies json: $dlJsonString")
        return JsonParsers.parseMovieDetailsJson(dlJsonString)
    }
}