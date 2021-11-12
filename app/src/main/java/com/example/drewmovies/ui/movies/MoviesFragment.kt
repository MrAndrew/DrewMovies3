package com.example.drewmovies.ui.movies

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.drewmovies.data.Movie
import com.example.drewmovies.databinding.FragmentMoviesBinding
import com.example.drewmovies.databinding.MovieListContentBinding
import com.example.drewmovies.utils.UrlBuilders
import com.squareup.picasso.Picasso
import android.content.Intent
import com.example.drewmovies.ui.utils.RoundedCornersTransformation
import android.app.ActivityOptions
import android.app.Activity
import androidx.core.content.ContextCompat.startActivity
import com.example.drewmovies.R
import android.os.Build
import androidx.recyclerview.widget.LinearLayoutManager
import java.text.SimpleDateFormat


class MoviesFragment : Fragment() {

    private var _binding: FragmentMoviesBinding? = null
    private var moviesViewModel: MoviesViewModel? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d("DEBUG", "onCreateView()")
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("DEBUG", "onViewCreated()")
        super.onViewCreated(view, savedInstanceState)

        /** Click Listener to trigger navigation based on if you have
         * a single pane layout or two pane layout
         */
        val onClickListener = View.OnClickListener { itemView ->
            val movie = itemView.tag as Movie
            val bundle = Bundle()
            bundle.putString(
                MovieDetailsActivity.ARG_ITEM_ID,
                movie?.id.toString()
            )
//                movie?.let { moviesViewModel?.selectMovie(it) }
            val sharedView: View = itemView.rootView.findViewById(R.id.image_preview)
            val transitionName = "selectedMoviePoster"
            val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(
                activity,
                sharedView,
                transitionName
            )

            val intent = Intent(activity, MovieDetailsActivity::class.java)
            intent.putExtra("MOVIE_ID", movie?.id)
            startActivity(intent, transitionActivityOptions.toBundle())

            val toast = Toast.makeText(context, "This will take you to the details.", LENGTH_SHORT)
            toast.show()
        }

        setupRecyclerView(binding.moviesList, onClickListener)

        initializeUI()
    }

    private fun initializeUI() {
        Log.d("DEBUG", "initializeUI() called")
        moviesViewModel = ViewModelProvider(requireActivity()).get(MoviesViewModel::class.java)
        if(moviesViewModel?.movies?.value?.size == 0) {
            moviesViewModel?.launchDataLoad()
        } else {
            moviesViewModel?.movies?.value ?: moviesViewModel?.launchDataLoad()
        }
        Log.d("DEBUG", "initializeUI() after launchDataLoad()")
        moviesViewModel?.movies?.observe(viewLifecycleOwner, { movies ->
            Log.d("DEBUG", "initializeUI()  moviesViewModel.movies.observe() movies: $movies")
            Log.d("DEBUG", "initializeUI()  moviesViewModel.movies.observe() binding.moviesList.adapter: ${binding.moviesList.adapter}")
            Log.d("DEBUG", "initializeUI()  moviesViewModel.movies.observe() binding.moviesList.adapter.itemCount: ${binding.moviesList.adapter?.itemCount}")
            //debug shows this as called, but not seeing it in the adapter
            val count = binding.moviesList.adapter?.itemCount ?: 0
            Log.d("DEBUG", "initializeUI() count $count")
            Log.d("DEBUG", "initializeUI() count $count")
            Log.d("DEBUG", "initializeUI() movies.size ${movies.size}")
            if(count > 0) {
                val subArray = (movies.slice(count until movies.size))
                if(subArray.isNotEmpty()) {
                    (binding.moviesList.adapter as SimpleItemRecyclerViewAdapter).addMovies(
                        subArray as ArrayList<Movie>
                    )
                } else {
                    //temporary fix b/c subarray is empty b/c binding.moviesList.adapter?.itemCount returns the new amount before adding them....
//                    binding.moviesList.adapter?.notifyDataSetChanged()
                    //todo fix this so not needed as it's hackish temporary workaround
                    binding.moviesList.adapter?.notifyItemRangeInserted(movies.size-20, movies.size)
                }
            } else {
                (binding.moviesList.adapter as SimpleItemRecyclerViewAdapter).setMovies(
                    movies
                )
            }

//            (binding.moviesList.adapter as SimpleItemRecyclerViewAdapter).notifyDataSetChanged()
            Log.d("DEBUG", "initializeUI()  moviesViewModel.movies.observe() binding.moviesList.adapter.itemCount: ${binding.moviesList.adapter?.itemCount}")
        })
        moviesViewModel?.isLoading?.observe(viewLifecycleOwner, { isLoading ->
            if(isLoading) {
                binding.moviesLoadingPb.visibility = View.VISIBLE
            } else {
                binding.moviesLoadingPb.visibility = View.INVISIBLE
            }
        })
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, onClickListener: View.OnClickListener) {
        Log.d("DEBUG", "setupRecyclerView() ${recyclerView.id}")
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(
            ArrayList<Movie>(),
            onClickListener
        )
        var scrollListener: RecyclerView.OnScrollListener =
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (moviesViewModel?.isLoading?.value == true) return
                    val visibleItemCount: Int = recyclerView.layoutManager?.childCount ?: 0
                    val totalItemCount: Int = recyclerView.layoutManager?.itemCount ?: 0
                    val pastVisibleItems = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
//                    if (pastVisibleItems + visibleItemCount >= totalItemCount-2) {
//                        //End of list
//                        Log.d("DEBUG", "RecyclerView bottom reached.")
//                        moviesViewModel?.launchDataLoad()
//                    }
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if(!recyclerView.canScrollVertically(1)) {
                        Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show()
                        moviesViewModel?.launchDataLoad()
                    }
                }
            }
        recyclerView.addOnScrollListener(scrollListener)
    }

    class SimpleItemRecyclerViewAdapter(
        private var values: ArrayList<Movie>,
        private var onClickListener: View.OnClickListener
    ) : RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            Log.d("DEBUG", "onCreateViewHolder() called")
            val binding =
                MovieListContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            Log.d("DEBUG", "onBindViewHolder() called")
            val movie = values[position]
            holder.movieTitleTv.text = movie?.movieTitle


            val calendar = movie?.releaseDate
            val format = SimpleDateFormat("MMM d, yyyy")
            val releaseDateString = format.format(calendar?.time)
            holder.movieReleaseDateTv.text = releaseDateString


            holder.movieRatingTv.text = movie?.userRating.toString()
            holder.aboutMovieTv.text = movie?.about
            val posterPath: String = movie?.imageUrlPath ?: "https://media.wired.com/photos/5dd593a829b9c40008b179b3/191:100/w_2338,h_1224,c_limit/Cul-BabyYoda_mandalorian-thechild-1_af408bfd.jpg"
            //Need to change to https as android only loads https by default and urls from server return http
            Log.d("DEBUG", "picasso image posterPath: $posterPath")
            val imagePosterUrl = UrlBuilders.buildImageRequestUrl(posterPath)
            Picasso.get().isLoggingEnabled = true
            Log.d("DEBUG", "picasso image loads: $imagePosterUrl")
            Picasso.get()
                .load(imagePosterUrl)
                .transform(RoundedCornersTransformation(10, 0))
                .placeholder(R.drawable.ic_baseline_downloading_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(holder.imagePreviewView)



            with(holder.itemView) {
                tag = movie
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(binding: MovieListContentBinding) :
            RecyclerView.ViewHolder(binding.root) {
            val movieTitleTv: TextView = binding.movieTitleTv
            val movieReleaseDateTv = binding.releaseDateTv
            val movieRatingTv = binding.userRatingTv
            val aboutMovieTv = binding.aboutTv
            val imagePreviewView: ImageView = binding.imagePreview
        }

        fun addMovies(movies: ArrayList<Movie>) {
            Log.d("DEBUG", "addMovies() in movies: $movies")
            Log.d("DEBUG", "addMovies() size before: ${values.size}")
            val lastPositionOld = values.size
            values.addAll(movies)
            notifyItemRangeInserted(lastPositionOld, values.size)
            Log.d("DEBUG", "addMovies() size after: ${values.size}")
        }

        @SuppressLint("NotifyDataSetChanged")
        fun setMovies(movies: ArrayList<Movie>) {
            Log.d("DEBUG", "setMovies() input: $movies")
            Log.d("DEBUG", "setMovies() values before: $values")
            Log.d("DEBUG", "setMovies() size before: ${values.size}")
            //this is called with correct movies input...
            values = movies
            notifyDataSetChanged()
            Log.d("DEBUG", "setMovies() values after: $values")
            Log.d("DEBUG", "setMovies() size after: ${values.size}")
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}