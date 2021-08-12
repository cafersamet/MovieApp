package com.gllce.mobilliummovieapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.gllce.mobilliummovieapp.R
import com.gllce.mobilliummovieapp.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.upComingMovies.observe(viewLifecycleOwner, { upComings ->
            upComings?.let {
                println("upComingMovies observeLiveData")
            }

        })

        viewModel.nowPlayingMovies.observe(viewLifecycleOwner, { nowPlaying ->
            nowPlaying?.let {
                println("nowPlaying observeLiveData")
            }

        })
    }

}