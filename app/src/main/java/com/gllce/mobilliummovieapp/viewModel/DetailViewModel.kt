package com.gllce.mobilliummovieapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gllce.mobilliummovieapp.model.Movie
import com.gllce.mobilliummovieapp.service.MovieApiRepository
import com.gllce.mobilliummovieapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val movieApiRepository: MovieApiRepository
) : ViewModel() {
    val movieDetail = MutableLiveData<Movie>()

    init {
        refreshData()
    }

    fun refreshData() {
        getUpComingMovies()
    }

    private fun getUpComingMovies() {
        viewModelScope.launch {
            when (val result = movieApiRepository.getMovieDetail()) {
                is Resource.Success -> {
                    movieDetail.value = result.data!!
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