package com.gllce.mobilliummovieapp.service

import com.gllce.mobilliummovieapp.model.Movie
import com.gllce.mobilliummovieapp.model.NowPlaying
import com.gllce.mobilliummovieapp.model.UpComing
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    // Base_Url -> https://api.themoviedb.org/3/movie/

    @GET("{id}")
    suspend fun getMovieDetail(
        @Path("id") id: Int
    ): Movie

    @GET("upcoming")
    suspend fun getUpComingMovies(
        @Query("page") page: Int,
    ): UpComing

    @GET("now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") page: Int,
    ): NowPlaying
}