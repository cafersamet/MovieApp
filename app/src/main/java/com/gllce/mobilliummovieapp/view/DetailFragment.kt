package com.gllce.mobilliummovieapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.gllce.mobilliummovieapp.R
import com.gllce.mobilliummovieapp.databinding.DetailFragmentBinding
import com.gllce.mobilliummovieapp.util.Resource
import com.gllce.mobilliummovieapp.viewModel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private var movieId: Int = 0
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var dataBinding: DetailFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.detail_fragment, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            movieId = DetailFragmentArgs.fromBundle(it).movieId
            println("Movie ID: $movieId")
        }
        viewModel.getMovieDetail(movieId)
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.movieDetail.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    //hideProgressBar()
                    println("Success")
                    response.data?.let { movie ->
                        println("nowPlaying observeLiveData")
                        dataBinding.movie = movie
                    }
                }
                is Resource.Loading -> {

                    //showProgressBar()
                    println("nowPlaying Loading")
                }
                is Resource.Error -> {
                    //hideProgressBar()
                    println("nowPlaying Error")
                    response.message?.let { message ->
                        Toast.makeText(context, "An error occurred: $message", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }

        })
    }

}