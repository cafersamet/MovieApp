package com.gllce.mobilliummovieapp.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gllce.mobilliummovieapp.model.Movie
import com.gllce.mobilliummovieapp.service.MovieApiRepository
import com.gllce.mobilliummovieapp.util.Resource
import com.gllce.mobilliummovieapp.util.isOnline
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val movieApiRepository: MovieApiRepository
) : ViewModel() {
    val movieDetail: MutableLiveData<Resource<Movie>> = MutableLiveData()

    fun getMovieDetail(id: Int) {
        viewModelScope.launch {
            movieDetail.postValue(Resource.Loading())
            if (!isOnline(context)) {
                movieDetail.postValue(Resource.Error("No Internet Connection"))
                return@launch
            }
            val response = movieApiRepository.getMovieDetail(id)
            movieDetail.postValue(handleMovieDetailResponse(response))
        }
    }

    private fun handleMovieDetailResponse(response: Response<Movie>): Resource<Movie>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}