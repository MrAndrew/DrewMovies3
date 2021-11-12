package com.example.drewmovies

import android.util.Log
import com.example.drewmovies.data.Movie
import com.example.drewmovies.ui.movies.MovieDetailsViewModel
import com.example.drewmovies.ui.movies.MoviesViewModel
import com.example.drewmovies.ui.movies.ReviewsViewModel
import junit.framework.TestCase
import kotlinx.coroutines.*
import org.junit.Assert
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class ViewModelTests: TestCase() {

    val moviesViewModel = MoviesViewModel()
    val testDummyMovie= Movie(
        123,
        "Baby Yoda Fights the Hulk",
        "https://media.allure.com/photos/5f8f3d745611fc41904f9aee/16:9/w_2560%2Cc_limit/baby%2520yoda.jpg",
        "",
        Calendar.getInstance(),
        100.0
    )
    val movieDetailsViewModel = MovieDetailsViewModel()
    val reviewsViewModel = ReviewsViewModel()

    @Test
    fun testSelectMovie() {
        runBlocking {
            launch(Dispatchers.Main) {
                moviesViewModel.selectMovie(testDummyMovie)
                Assert.assertEquals(moviesViewModel.selectedMovie.value, testDummyMovie)
//                Assert.fail()
            }
        }
    }

    @Test
    fun testLoadMovies() {
        runBlocking {
            moviesViewModel.launchDataLoad()
            delay(3000) //needed b/c loading from url
            Assert.assertTrue(moviesViewModel.movies.value?.size ?: 0 > 0)
//            Assert.fail()
        }
    }

    @Test
    fun testGetMovieDetails() {
        runBlocking {
            launch(Dispatchers.Main) {
                movieDetailsViewModel.launchLoadMovieDetails("123")
                delay(5000) //needed b/c loading from url
                Log.d("TEST", "${movieDetailsViewModel.movieDetails.value}")
                Assert.assertEquals(movieDetailsViewModel.movieDetails.value?.title, "The Lord of the Rings")
                Assert.assertEquals(movieDetailsViewModel.movieDetails.value?.genres, listOf("Adventure", "Animation", "Fantasy"))
                Assert.assertEquals(movieDetailsViewModel.movieDetails.value?.runtime, 132)
                Assert.assertEquals(movieDetailsViewModel.movieDetails.value?.revenue, 30471420)
//                Assert.fail()
            }
        }
    }

    @Test
    fun testGetMovieReviews() {
        runBlocking {
            launch(Dispatchers.Main) {
                reviewsViewModel.launchLoadMovieReviews(438631)
                delay(3000) //needed b/c loading from url
                Log.d("TEST", "${reviewsViewModel.reviews.value}")
                Assert.assertTrue(reviewsViewModel.reviews.value?.size ?: 0 > 0)
//                Assert.fail()
            }
        }
    }

}