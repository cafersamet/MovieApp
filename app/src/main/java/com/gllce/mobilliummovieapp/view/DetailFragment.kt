package com.gllce.mobilliummovieapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.gllce.mobilliummovieapp.R
import com.gllce.mobilliummovieapp.viewModel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {


    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            println("Movie ID: " + DetailFragmentArgs.fromBundle(it).movieId)
        }

        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.movieDetail.observe(viewLifecycleOwner, { movie ->
            movie?.let {
                println("movie detail observeLiveData")
            }

        })
    }

}