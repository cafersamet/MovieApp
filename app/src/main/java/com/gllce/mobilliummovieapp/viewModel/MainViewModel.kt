package com.gllce.mobilliummovieapp.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gllce.mobilliummovieapp.model.NowPlaying
import com.gllce.mobilliummovieapp.model.UpComing
import com.gllce.mobilliummovieapp.service.MovieApiRepository
import com.gllce.mobilliummovieapp.util.Resource
import com.gllce.mobilliummovieapp.util.isOnline
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val movieApiRepository: MovieApiRepository
) : ViewModel() {
    var upComingPage = 1
    val upComingMovies: MutableLiveData<Resource<UpComing>> = MutableLiveData()
    var upComingResponse: UpComing? = null

    val nowPlayingMovies: MutableLiveData<Resource<NowPlaying>> = MutableLiveData()

    init {
        refreshData(true)
    }

    fun refreshData(isRefresh: Boolean) {
        if (isRefresh) {
            upComingPage = 1
        }
        getUpComingMovies()
        getNowPlayingMovies()
    }

    fun getUpComingMovies() {
        viewModelScope.launch {
            upComingMovies.postValue(Resource.Loading())
            if (!isOnline(context)) {
                upComingMovies.postValue(Resource.Error("No Internet Connection"))
                return@launch
            }
            val response = movieApiRepository.getUpComingMovies(upComingPage)
            upComingMovies.postValue(handleUpComingMoviesResponse(response))
        }
    }

    private fun handleUpComingMoviesResponse(response: Response<UpComing>): Resource<UpComing>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (upComingResponse == null || upComingPage == 1) {
                    upComingResponse = resultResponse
                } else {
                    val oldMovies = upComingResponse?.results
                    val newMovies = resultResponse.results
                    oldMovies?.addAll(newMovies)
                }
                upComingPage++
                return Resource.Success(upComingResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun getNowPlayingMovies() {
        viewModelScope.launch {
            nowPlayingMovies.postValue(Resource.Loading())
            if (!isOnline(context)) {
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