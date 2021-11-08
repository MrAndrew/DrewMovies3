package com.example.drewmovies.ui.movies

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.drewmovies.data.MovieDetails
import com.example.drewmovies.utils.APICallers
import com.example.drewmovies.utils.UrlBuilders
import kotlinx.coroutines.*
import java.net.URL

class MovieDetailsViewModel : ViewModel() {

    val TAG = this::class.simpleName

    var movieDetails = MutableLiveData<MovieDetails>()

    //for all coroutines started by this ViewModel
    private val viewModelJob = SupervisorJob() //supervisor job allows app not to crash if one network call fails
    //main scope for all coroutines launched by this MainViewModel
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun launchLoadMovieDetails(id: String) {
        Log.d("DEBUG", "launchLoadMovieDetails() id: $id")
        uiScope.launch {
            val movieDetailsData = getMovieDetails(id)
            // Modify UI
            Log.d("DEBUG", "movieDetailsData to be set: $movieDetailsData")
            movieDetails.value = movieDetailsData
        }
    }

    suspend fun getMovieDetails(id: String) = withContext(Dispatchers.Default) {
        Log.d("DEBUG", "getMovieDetails() id: $id")
        val detailsUrl =
            UrlBuilders.buildMovieDetailsRequestUrl(id) ?: URL("")
        val movieDetails = APICallers.loadMovieDetailsFromUrl(detailsUrl)
        return@withContext movieDetails
    }

    //Cancel all coroutines when the ViewModel is cleared
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}