package com.example.drewmovies.ui.movies

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.Nullable
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.drewmovies.R
import com.example.drewmovies.data.Review
import com.example.drewmovies.databinding.FragmentBottomMovieReviewsBinding
import com.example.drewmovies.databinding.ReviewListContentBinding
import com.example.drewmovies.ui.utils.CircleTransform
import com.example.drewmovies.utils.UrlBuilders

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class ReviewsBottomDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomMovieReviewsBinding? = null
    private var reviewsViewModel: ReviewsViewModel? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var movieID = 0

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        movieID =  arguments?.getInt("movie_id", 0) ?: 0
        _binding = FragmentBottomMovieReviewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(binding.reviewsList)
        initializeUI()
    }

    private fun initializeUI() {
        Log.d("DEBUG", "initializeUI() called")
        reviewsViewModel = ViewModelProvider(requireActivity()).get(ReviewsViewModel::class.java)
        if(reviewsViewModel?.reviews?.value?.size == 0) {
            reviewsViewModel?.launchLoadMovieReviews(movieID)
        } else {
            reviewsViewModel?.reviews?.value ?: reviewsViewModel?.launchLoadMovieReviews(movieID)
        }
        Log.d("DEBUG", "initializeUI() after launchDataLoad()")
        reviewsViewModel?.reviews?.observe(viewLifecycleOwner, { reviews ->
            (binding.reviewsList.adapter as SimpleItemRecyclerViewAdapter).setReviews(
                reviews
            )
        })
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        Log.d("DEBUG", "setupRecyclerView() ${recyclerView.id}")
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(
            listOf()
        )
    }

    class SimpleItemRecyclerViewAdapter(private var values: List<Review?>) : RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            Log.d("DEBUG", "onCreateViewHolder() called")
            val binding =
                ReviewListContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            Log.d("DEBUG", "onBindViewHolder() called")
            val review = values[position]
            holder.reviewerUsernameTv.text = review?.reviewAuthor
            var rating = review?.rating
            if(rating == 0) {
                holder.reviewRatingTv.text = ""
                holder.starsIv.visibility = View.INVISIBLE
            } else {
                holder.reviewRatingTv.text = review?.rating.toString()
                holder.starsIv.visibility = View.VISIBLE
            }
            if(holder.reviewRatingTv.text == "0") holder.reviewRatingTv.text
            holder.reviewerContentTv.text = review?.reviewContent

            val calendar = review?.reviewDate
            val format = SimpleDateFormat("EEEE, MMMM d, yyyy")
            val reviewDateString = format.format(calendar?.time ?: Date())
            holder.reviewDateTv.text = reviewDateString

            val avatarPath = review?.authorAvatarPath ?: ""
            val reviewerPicUrl = UrlBuilders.buildUserAvatarImageRequestUrl(avatarPath)

            Picasso.get().isLoggingEnabled = true
            Log.d("DEBUG", "picasso image loads: $reviewerPicUrl")
            Picasso.get()
                .load(reviewerPicUrl)
                .transform(CircleTransform())
                .placeholder(R.drawable.ic_baseline_downloading_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(holder.reviewerProfilePicIv)

        }

        override fun getItemCount() = values.size

        inner class ViewHolder(binding: ReviewListContentBinding) :
            RecyclerView.ViewHolder(binding.root) {
            val reviewerProfilePicIv = binding.reviewerProfileIv
            val reviewerUsernameTv = binding.reviewerUsernameTv
            val reviewRatingTv = binding.reviewRatingTv
            val reviewerContentTv = binding.reviewerContentTv
            val reviewDateTv = binding.reviewDateTv
            val starsIv = binding.starsIv
        }

        @SuppressLint("NotifyDataSetChanged")
        fun setReviews(reviews: List<Review>) {
            //this is called with correct movies input...
            values = reviews
            notifyDataSetChanged()
        }

    }

    companion object {
        fun newInstance(): ReviewsBottomDialogFragment {
            return ReviewsBottomDialogFragment()
        }
    }
}