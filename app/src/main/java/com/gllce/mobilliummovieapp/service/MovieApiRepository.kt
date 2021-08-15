package com.gllce.mobilliummovieapp.service

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.gllce.mobilliummovieapp.model.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val PAGE_SIZE = 20

class MovieApiRepository @Inject constructor(private val movieApi: MovieApi) {

    fun getUpComingMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                maxSize = PAGE_SIZE + (PAGE_SIZE * 2),
                enablePlaceholders = false
            ),
            pagingSourceFactory = { UpComingPagingSource(movieApi) }
        ).flow
    }

    suspend fun getNowPlayingMovies(page: Int) = movieApi.getNowPlayingMovies(page)

    suspend fun getMovieDetail(id: Int) = movieApi.getMovieDetail(id)

}