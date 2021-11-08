package com.example.drewmovies.ui.movies

import android.os.Bundle
import android.transition.Explode
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.drewmovies.R

import android.view.MenuItem
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.marginStart
import androidx.lifecycle.ViewModelProvider
import androidx.ui.res.dimensionResource
import com.example.drewmovies.data.MovieDetails
import com.example.drewmovies.databinding.ActivityMovieDetailsBinding
import com.example.drewmovies.ui.utils.RoundedCornersTransformation
import com.example.drewmovies.utils.UrlBuilders
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat


class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailsBinding
    private var movieDetailsViewModel: MovieDetailsViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movieID = intent.getIntExtra("MOVIE_ID", 0).toString()

        Log.d("DEBUG", "onCreate() movieID: $movieID")
        loadDetailsUI(movieID)
    }

    fun loadDetailsUI(movieID: String?) {
        Log.d("DEBUG", "loadDetailsUI() movieID: $movieID")
        movieDetailsViewModel = ViewModelProvider(this)[MovieDetailsViewModel::class.java]

        Log.d("DEBUG", "loadDetailsUI() movieDetailsViewModel.movieDetails.value: ${movieDetailsViewModel?.movieDetails?.value} ")
        movieDetailsViewModel?.movieDetails?.observe(this, { movieDetails ->
            //debug shows this as called, but not seeing it in the adapter
            Log.d("DEBUG", "movieDetails in loadDetailsUI(): $movieDetails")
            setUIContent(movieDetails)
        })

        Log.d("DEBUG", "loadDetailsUI() movieDetailsViewModel.movieDetails.value: ${movieDetailsViewModel?.movieDetails?.value} ")

        movieDetailsViewModel?.launchLoadMovieDetails(movieID ?: "")
    }

    fun setUIContent(movieDetails: MovieDetails) {
        Log.d("DEBUG", "movieDetails: $movieDetails")
        val posterPath: String = movieDetails.imageUrlPath
        //Need to change to https as android only loads https by default and urls from server return http
        Log.d("DEBUG", "picasso image posterPath: $posterPath")
        val imagePosterUrl = UrlBuilders.buildImageRequestUrl(posterPath, "large")
        Picasso.get().isLoggingEnabled = true
        Log.d("DEBUG", "picasso image loads: $imagePosterUrl")
        val aspectRatio = 16.0/9.0
        Log.d("stuff", "aspectRatio: $aspectRatio")
        val width = binding.movieIv.width
        Log.d("stuff", "width: $width")
        val height = width/aspectRatio
        Log.d("stuff", "height: $height")
        Picasso.get()
            .load(imagePosterUrl)
            .resize(width, height.toInt())
            .transform(RoundedCornersTransformation(10, 0))
//            .placeholder(R.drawable.ic_baseline_downloading_24)
            .error(R.drawable.ic_baseline_broken_image_24)
            .into(binding.movieIv)
        binding.titleTv.text = movieDetails.title
        val calendar = movieDetails.releaseDate
        val format = SimpleDateFormat("EEEE, MMMM d, yyyy")
        val releaseDateString = format.format(calendar.time)
        binding.releaseDateTv.text = releaseDateString
        Log.d("stuff", "genres: ${movieDetails.genres}")
        movieDetails.genres.forEach {
            val tv = TextView(this)
            tv.text = it
            tv.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
            tv.background = ContextCompat.getDrawable(this, R.drawable.tag_background)
            tv.setTextColor(ContextCompat.getColor(this, R.color.off_white_text_color))
            tv.setPadding(30, 20, 30, 20)

            val params = tv.layoutParams as LinearLayout.LayoutParams
            params.setMargins(15, 0, 15, 0) //substitute parameters for left, top, right, bottom
            tv.layoutParams = params

            binding.genresLl.addView(tv)
        }

        binding.overviewTv.text = movieDetails.overview

        binding.seeReviewsButton.setOnClickListener {
            this.showReviews()
        }
    }

    fun showReviews() {
        val reviewsBottomDialogFragment: ReviewsBottomDialogFragment =
            ReviewsBottomDialogFragment.newInstance()
        val bundle = Bundle()
        movieDetailsViewModel?.movieDetails?.value?.id?.let { bundle.putInt("movie_id", it) }
        reviewsBottomDialogFragment.arguments = bundle
        reviewsBottomDialogFragment.show(
            supportFragmentManager,
            "show_reviews_dialog_fragment"
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    companion object {
        const val ARG_ITEM_ID = "movie_id"
    }

}