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
    var isLoading = MutableLiveData<Boolean>(false)
    var pagePaginationNumber = MutableLiveData<Int>(0)

    //for all coroutines started by this ViewModel
    private val viewModelJob = SupervisorJob() //supervisor job allows app not to crash if one network call fails
    //main scope for all coroutines launched by this MainViewModel
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    //Heavy operation that cannot be done in the Main Thread
    fun launchDataLoad() {
        Log.d("DEBUG", "launchDataLoad()")
        uiScope.launch {
            isLoading.value = true
            var numberCopy = pagePaginationNumber.value
            numberCopy = numberCopy ?: 0
            numberCopy++
            pagePaginationNumber.value = numberCopy!!
            val url = UrlBuilders.buildMovieListPopRequestUrl(numberCopy)
            Log.d("DEBUG", "launchDataLoad() Movies url: $url")
            val moviesList = getPopularMovies(url.toString())
            // Modify UI
            Log.d("DEBUG", "launchDataLoad() movies.value to be set: $moviesList")
//            val prevMovies = movies.value
//            prevMovies?.addAll(moviesList)
            movies.plusAssign(moviesList)
            isLoading.value = false
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

    //Cancel all coroutines when the ViewModel is cleared
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    operator fun <T> MutableLiveData<ArrayList<T>>.plusAssign(values: List<T>) {
        val value = this.value ?: arrayListOf()
        value.addAll(values)
        this.value = value
    }

}