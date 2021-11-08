package com.example.drewmovies.ui.movies

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.drewmovies.data.Movie
import com.example.drewmovies.data.MovieDetails
import com.example.drewmovies.utils.APICallers
import com.example.drewmovies.utils.UrlBuilders
import kotlinx.coroutines.*

class MoviesViewModel : ViewModel() {

    val TAG = this::class.simpleName

    var movies = MutableLiveData<ArrayList<Movie>>()
    var selectedMovie = MutableLiveData<Movie>()
    var selectedMovieDetails = MutableLiveData<MovieDetails>()

    //for all coroutines started by this ViewModel
    private val viewModelJob = SupervisorJob() //supervisor job allows app not to crash if one network call fails
    //main scope for all coroutines launched by this MainViewModel
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    //Heavy operation that cannot be done in the Main Thread
    fun launchDataLoad() {
        Log.d("DEBUG", "launchDataLoad()")
        uiScope.launch {
            val url = UrlBuilders.buildMovieListPopRequestUrl()
            Log.d("DEBUG", "Movies returned: $url")
            val moviesList = getPopularMovies(url.toString())
            // Modify UI
            Log.d("DEBUG", "movies.value to be set: $moviesList")
            movies.value = moviesList
        }
    }

    suspend fun getPopularMovies(url: String) = withContext(Dispatchers.Default) {
        Log.d(TAG, "getPopularMovies()")

        val movies = APICallers.loadMoviesJsonFromUrl(url)
        Log.d("DEBUG", "getPopularMovies() Movies returned from API Caller in getPopularMovies(): $movies")

        return@withContext movies
    }

    fun selectMovie(movie: Movie) {
        selectedMovie.value = movie
//        launchLoadMovieDetails(movie.id.toString())
    }

//    fun launchLoadMovieDetails(id: String) {
//        uiScope.launch {
//            val movieDetails = getMovieDetails(id)
//            // Modify UI
//            Log.d("DEBUG", "movieDetails to be set: $movieDetails")
//            selectedMovieDetails.value = movieDetails
//        }
//    }

//    suspend fun getMovieDetails(id: String) = withContext(Dispatchers.Default) {
//        Log.d("DEBUG", "getMovieDetails()")
//        val detailsUrl =
//            UrlBuilders.buildMovieDetailsRequestUrl(id)
//        val movieDetails = detailsUrl?.let { APICallers.loadMovieDetailsFromUrl(it) }
//        return@withContext movieDetails ?: MovieDetails()
//    }

    //Cancel all coroutines when the ViewModel is cleared
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}