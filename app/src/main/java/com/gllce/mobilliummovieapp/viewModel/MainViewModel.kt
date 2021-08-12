package com.gllce.mobilliummovieapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gllce.mobilliummovieapp.model.Movie
import com.gllce.mobilliummovieapp.model.NowPlaying
import com.gllce.mobilliummovieapp.model.Upcoming
import com.gllce.mobilliummovieapp.service.MovieApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val movieApiRepository: MovieApiRepository
) : ViewModel() {
    val upComingMovies = MutableLiveData<List<Upcoming>>()
    val nowPlayingMovies = MutableLiveData<List<NowPlaying>>()

    private val disposable = CompositeDisposable()

    fun refreshData() {
        getUpComingMovies()
        getNowPlayingMovies()
        getMovieDetail()
    }

    private fun getMovieDetail() {
        disposable.add(
            movieApiRepository.getMovieDetail()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Movie>() {
                    override fun onSuccess(t: Movie) {
                        println(
                            "onSuccess"
                        )
                    }

                    override fun onError(e: Throwable) {
                        println(
                            "onError"
                        )
                    }
                })
        )
    }

    private fun getUpComingMovies() {
        disposable.add(
            movieApiRepository.getUpComingMovies()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Upcoming>() {
                    override fun onSuccess(t: Upcoming) {
                        println(
                            "onSuccess"
                        )
                    }

                    override fun onError(e: Throwable) {
                        println(
                            "onError"
                        )
                    }
                })
        )
    }

    private fun getNowPlayingMovies() {
        disposable.add(
            movieApiRepository.getNowPlayingMovies()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<NowPlaying>() {
                    override fun onSuccess(t: NowPlaying) {
                        println(
                            "onSuccess"
                        )
                    }

                    override fun onError(e: Throwable) {
                        println(
                            "onError"
                        )
                    }
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}