package com.gllce.mobilliummovieapp.service

import com.gllce.mobilliummovieapp.model.Movie
import com.gllce.mobilliummovieapp.model.NowPlaying
import com.gllce.mobilliummovieapp.model.Upcoming
import io.reactivex.Single
import javax.inject.Inject

class MovieApiRepository @Inject constructor(private val movieApi: MovieApi) {

    fun getUpComingMovies(): Single<Upcoming> {
        return movieApi.getUpComingMovies()
    }

    fun getNowPlayingMovies(): Single<NowPlaying> {
        return movieApi.getNowPlayingMovies()
    }

    fun getMovieDetail(): Single<Movie> {
        return movieApi.getMovieDetail()
    }
}