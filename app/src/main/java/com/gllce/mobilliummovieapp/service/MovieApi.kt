package com.gllce.mobilliummovieapp.service

import com.gllce.mobilliummovieapp.model.Movie
import com.gllce.mobilliummovieapp.model.NowPlaying
import com.gllce.mobilliummovieapp.model.Upcoming
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    // Base_Url -> https://api.themoviedb.org/3/movie/

    @GET("{id}")
    fun getMovieDetail(
        @Path("id") id: Int
    ): Single<Movie>

    @GET("upcoming")
    fun getUpComingMovies(
        @Query("page") page: Int,
    ): Single<Upcoming>

    @GET("now_playing")
    fun getNowPlayingMovies(
        @Query("page") page: Int,
    ): Single<NowPlaying>
}