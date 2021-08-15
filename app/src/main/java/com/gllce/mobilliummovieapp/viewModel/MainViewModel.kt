package com.gllce.mobilliummovieapp.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.gllce.mobilliummovieapp.model.NowPlaying
import com.gllce.mobilliummovieapp.service.MovieApiRepository
import com.gllce.mobilliummovieapp.util.Resource
import com.gllce.mobilliummovieapp.util.isNetworkConnected
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val application: Application,
    private val movieApiRepository: MovieApiRepository
) : ViewModel() {
    var upComingPagingList = movieApiRepository.getUpComingMovies().cachedIn(viewModelScope)
    val nowPlayingMovies: MutableLiveData<Resource<NowPlaying>> = MutableLiveData()

    fun getNowPlayingMovies() {
        viewModelScope.launch {
            nowPlayingMovies.postValue(Resource.Loading())
            if (!isNetworkConnected(application.applicationContext)) {
                nowPlayingMovies.postValue(Resource.Error("No Internet Connection"))
                return@launch
            }
            val response = movieApiRepository.getNowPlayingMovies(1)
            nowPlayingMovies.postValue(handleNowPlayingMoviesResponse(response))
        }
    }

    private fun handleNowPlayingMoviesResponse(response: Response<NowPlaying>): Resource<NowPlaying>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}