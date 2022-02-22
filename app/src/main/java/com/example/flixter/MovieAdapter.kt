package com.example.flixter

import android.content.Context
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MovieAdapter(private val context: Context, private val movies: List<Movie>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val POPULAR = 0
    private val UNPOPULAR = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            POPULAR -> {
                val vPopular: View = inflater.inflate(R.layout.item_movie_popular, parent, false)
                viewHolder = ViewHolderPopular(vPopular)
            }
            UNPOPULAR -> {
                val vUnpopular: View =
                    inflater.inflate(R.layout.item_movie_unpopular, parent, false)
                viewHolder = ViewHolderUnpopular(vUnpopular)
            }
            else -> {
                val vUnpopular: View =
                    inflater.inflate(R.layout.item_movie_unpopular, parent, false)
                viewHolder = ViewHolderUnpopular(vUnpopular)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            POPULAR -> {
                val vhPopular = holder as ViewHolderPopular
                configureViewHolderPopular(vhPopular, position)
            }
            UNPOPULAR -> {
                val vhUnpopular = holder as ViewHolderUnpopular
                configureViewHolderUnpopular(vhUnpopular, position)
            }
            else -> {
                val vhUnpopular = holder as ViewHolderUnpopular
                configureViewHolderUnpopular(vhUnpopular, position)
            }
        }
    }

    override fun getItemCount() = movies.size

    override fun getItemViewType(position: Int): Int {
        //if movie is voted 5+ stars, use ViewHolderPopular, else ViewHolderUnpopular
        return if (movies[position].voteAverage >= 5.0) {
            POPULAR
        } else {
            UNPOPULAR
        }
    }

    class ViewHolderPopular(v: View) : RecyclerView.ViewHolder(v) {
        val ivBackdrop: ImageView

        init {
            ivBackdrop = v.findViewById(R.id.ivBackdrop)
        }
    }

    private fun configureViewHolderPopular(vh: ViewHolderPopular, position: Int) {
        val movie = movies[position]

        Glide.with(context)
            .load(movie.posterBackdropUrl)
            .placeholder(R.drawable.placeholder)
            .into(vh.ivBackdrop)
    }

    class ViewHolderUnpopular(v: View) : RecyclerView.ViewHolder(v) {
        val ivPoster: ImageView
        val tvTitle: TextView
        val tvOverview: TextView

        init {
            ivPoster = v.findViewById(R.id.ivPoster)
            tvTitle = v.findViewById(R.id.tvTitle)
            tvOverview = v.findViewById(R.id.tvOverview)
        }
    }

    private fun configureViewHolderUnpopular(vh: ViewHolderUnpopular, position: Int) {
        val movie = movies[position]

        vh.tvTitle.text = movie.title
        vh.tvOverview.text = movie.overview

        val orientation = context.resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            Glide.with(context).load(movie.posterImageUrl).into(vh.ivPoster)
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Glide.with(context)
                .load(movie.posterBackdropUrl)
                .placeholder(R.drawable.placeholder)
                .into(vh.ivPoster)
        }
    }
}
