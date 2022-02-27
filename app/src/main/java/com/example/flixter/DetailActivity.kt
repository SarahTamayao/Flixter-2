package com.example.flixter

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.flixter.databinding.ActivityDetailBinding
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import okhttp3.Headers

private const val TRAILERS_URL =
    "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"
private const val TAG = "DetailActivity"

class DetailActivity : YouTubeBaseActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        val movie = intent.getParcelableExtra<Movie>(MOVIE_EXTRA) as Movie
        //title, overview, and rating are set using data binding
        binding.movie = movie

        //todo if movie rating is 5+ stars, play video immediately
        //     else show an image preview that can initiate playing a YT vid

        val client = AsyncHttpClient()
        client.get(TRAILERS_URL.format(movie.movieId), object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.i(TAG, "onSuccess")
                val results = json.jsonObject.getJSONArray("results")
                if (results.length() == 0) {
                    Log.w(TAG, "No movie trailers found")
                    return
                }
                val movieTrailerJson = results.getJSONObject(0)
                val youtubeKey = movieTrailerJson.getString("key")
                //play yt video with this trailer
                initializeYoutube(youtubeKey, movie.voteAverage)
            }

            override fun onFailure(
                statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?
            ) {
                Log.i(TAG, "onFailure")
            }
        })
    }

    private fun initializeYoutube(youtubeKey: String, voteAverage: Float) {
        binding.player.initialize(
            resources.getString(R.string.youtube_api_key),
            object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(
                    provider: YouTubePlayer.Provider?,
                    player: YouTubePlayer?,
                    p2: Boolean
                ) {
                    Log.i(TAG, "onInitializationSuccess")
                    //if movie is popular, play vid immediately
                    if (voteAverage > 5) {
                        player?.loadVideo(youtubeKey)
                    } else {
                        player?.cueVideo(youtubeKey)
                    }
                }

                override fun onInitializationFailure(
                    p0: YouTubePlayer.Provider?,
                    p1: YouTubeInitializationResult?
                ) {
                    Log.i(TAG, "onInitializationFailure: $p1 \n $p0")
                }
            })
    }
}