package com.example.drewmovies.ui.movies

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.drewmovies.data.Movie
import com.example.drewmovies.data.MovieDetails
import com.example.drewmovies.data.Review
import com.example.drewmovies.utils.APICallers
import com.example.drewmovies.utils.UrlBuilders
import kotlinx.coroutines.*
import java.net.URL

class ReviewsViewModel: ViewModel() {

    val TAG = this::class.simpleName

    var reviews = MutableLiveData<ArrayList<Review>>()

    //for all coroutines started by this ViewModel
    private val viewModelJob = SupervisorJob() //supervisor job allows app not to crash if one network call fails
    //main scope for all coroutines launched by this MainViewModel
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun launchLoadMovieReviews(id: Int) {
        Log.d("DEBUG", "launchLoadMovieReviews() id: $id")
        uiScope.launch {
            val movieReviews = getMovieReviews(id)
            // Modify UI
            Log.d("DEBUG", "launchLoadMovieReviews() movieReviews to be set: $movieReviews")
            reviews.value = movieReviews
        }
    }

    suspend fun getMovieReviews(id: Int) = withContext(Dispatchers.Default) {
        Log.d("DEBUG", "getMovieReviews() id: $id")
        val reviewsUrl =
            UrlBuilders.buildMovieReviewsRequestUrl(id.toString()) ?: URL("")
        val movieReviews = APICallers.loadReviews(reviewsUrl)
        return@withContext movieReviews
    }

    //Cancel all coroutines when the ViewModel is cleared
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}