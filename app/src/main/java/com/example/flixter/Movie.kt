package com.example.flixter

import org.json.JSONArray

data class Movie(
    val movieId: Int,
    private val posterPath: String,
    val title: String,
    val overview: String
) {
    val posterImageUrl = "https://image.tmdb.org/t/p/w342$posterPath"

    companion object {
        fun fromJsonArray(movieJsonArray: JSONArray) : List<Movie> {
            val movies = mutableListOf<Movie>()

            for (i in 0 until movieJsonArray.length()) {
                val movieJson = movieJsonArray.getJSONObject(i)
                movies.add(
                    Movie(
                        movieId = movieJson.getInt("id"),
                        posterPath = movieJson.getString("poster_path"),
                        title = movieJson.getString("title"),
                        overview = movieJson.getString("overview")
                    )
                )
            }

            return movies
        }
    }
}
