package com.example.flixter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.json.JSONArray

@Parcelize
data class Movie(
    val movieId: Int,
    private val posterPath: String,
    private val backdropPath: String,
    val title: String,
    val overview: String,
    val voteAverage: Double
) : Parcelable {
    val posterImageUrl = "https://image.tmdb.org/t/p/w342$posterPath"
    val posterBackdropUrl = "https://image.tmdb.org/t/p/w780$backdropPath"

    companion object {
        fun fromJsonArray(movieJsonArray: JSONArray) : List<Movie> {
            val movies = mutableListOf<Movie>()

            for (i in 0 until movieJsonArray.length()) {
                val movieJson = movieJsonArray.getJSONObject(i)
                movies.add(
                    Movie(
                        movieId = movieJson.getInt("id"),
                        posterPath = movieJson.getString("poster_path"),
                        backdropPath = movieJson.getString("backdrop_path"),
                        title = movieJson.getString("title"),
                        overview = movieJson.getString("overview"),
                        voteAverage = movieJson.getDouble("vote_average")
                    )
                )
            }

            return movies
        }
    }
}
