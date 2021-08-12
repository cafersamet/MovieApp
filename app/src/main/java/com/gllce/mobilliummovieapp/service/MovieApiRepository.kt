package com.gllce.mobilliummovieapp.service

import com.gllce.mobilliummovieapp.model.Movie
import com.gllce.mobilliummovieapp.model.NowPlaying
import com.gllce.mobilliummovieapp.model.UpComing
import com.gllce.mobilliummovieapp.util.Resource
import javax.inject.Inject

class MovieApiRepository @Inject constructor(private val movieApi: MovieApi) {

    suspend fun getUpComingMovies(): Resource<UpComing> {
        val response = try {
            movieApi.getUpComingMovies(1)
        } catch (e: Exception) {
            return Resource.Error("Error while getting UpComingMovies")
        }
        return Resource.Success(response)
    }

    suspend fun getNowPlayingMovies(): Resource<NowPlaying> {
        val response = try {
            movieApi.getNowPlayingMovies(1)
        } catch (e: Exception) {
            return Resource.Error("Error while getting NowPlayingMovies")
        }
        return Resource.Success(response)
    }

    suspend fun getMovieDetail(): Resource<Movie> {
        val response = try {
            movieApi.getMovieDetail(550)
        } catch (e: Exception) {
            return Resource.Error("Error while getting MovieDetail")
        }
        return Resource.Success(response)
    }
}