package com.gllce.mobilliummovieapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.gllce.mobilliummovieapp.R
import com.gllce.mobilliummovieapp.adapter.UpComingAdapter
import com.gllce.mobilliummovieapp.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_fragment.*


@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private val upComingAdapter = UpComingAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        upComingRecyclerView.layoutManager = LinearLayoutManager(context)
        upComingRecyclerView.adapter = upComingAdapter

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshData()
            upComingRecyclerView.visibility = View.GONE
            swipeRefreshLayout.isRefreshing = false
            upComingLoading.visibility = View.VISIBLE
        }

        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.upComingMovies.observe(viewLifecycleOwner, { upComings ->
            upComings?.let {
                upComingAdapter.updateList(upComings.results)
                upComingRecyclerView.visibility = View.VISIBLE
                upComingLoading.visibility = View.GONE
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