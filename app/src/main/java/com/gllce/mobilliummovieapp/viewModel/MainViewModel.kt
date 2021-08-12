package com.gllce.mobilliummovieapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gllce.mobilliummovieapp.model.NowPlaying
import com.gllce.mobilliummovieapp.model.UpComing
import com.gllce.mobilliummovieapp.service.MovieApiRepository
import com.gllce.mobilliummovieapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val movieApiRepository: MovieApiRepository
) : ViewModel() {
    val upComingMovies = MutableLiveData<UpComing>()
    val nowPlayingMovies = MutableLiveData<NowPlaying>()

    init {
        refreshData()
    }

    fun refreshData() {
        getUpComingMovies()
        getNowPlayingMovies()
    }

    private fun getUpComingMovies() {
        viewModelScope.launch {
            when (val result = movieApiRepository.getUpComingMovies()) {
                is Resource.Success -> {
                    upComingMovies.value = result.data!!
                }
                is Resource.Error -> {
                    TODO()
                }
                is Resource.Loading -> {
                    TODO()
                }
            }
        }
    }

    private fun getNowPlayingMovies() {
        viewModelScope.launch {
            when (val result = movieApiRepository.getNowPlayingMovies()) {
                is Resource.Success -> {
                    nowPlayingMovies.value = result.data!!
                }
                is Resource.Error -> {
                    TODO()
                }
                is Resource.Loading -> {
                    TODO()
                }
            }
        }
    }
}