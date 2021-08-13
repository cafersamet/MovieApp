package com.gllce.mobilliummovieapp.service

import javax.inject.Inject

class MovieApiRepository @Inject constructor(private val movieApi: MovieApi) {

    suspend fun getUpComingMovies(page: Int) = movieApi.getUpComingMovies(page)

    suspend fun getNowPlayingMovies(page: Int) = movieApi.getNowPlayingMovies(page)

    suspend fun getMovieDetail(id: Int) = movieApi.getMovieDetail(id)

}