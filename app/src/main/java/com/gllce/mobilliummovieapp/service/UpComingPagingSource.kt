package com.gllce.mobilliummovieapp.service

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gllce.mobilliummovieapp.model.Movie
import java.io.IOException
import javax.inject.Inject

private const val START_PAGE_INDEX = 1

class UpComingPagingSource @Inject constructor(private val api: MovieApi) :
    PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: START_PAGE_INDEX
        try {
            val response = api.getUpComingMovies(page = page)
            if (response.isSuccessful) {
                val result = response.body()
                result?.let {
                    return LoadResult.Page(
                        data = result.results,
                        prevKey = if (page == START_PAGE_INDEX) null else page - 1,
                        nextKey = if (result.results.isEmpty()) null else page + 1
                    )
                }
                return LoadResult.Error(Exception("Failed to get response"))
            } else {
                return LoadResult.Error(Exception("Some error occurred"))
            }
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return 1
    }
}