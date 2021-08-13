package com.gllce.mobilliummovieapp.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gllce.mobilliummovieapp.R
import com.gllce.mobilliummovieapp.adapter.NowPlayingSliderAdapter
import com.gllce.mobilliummovieapp.adapter.UpComingAdapter
import com.gllce.mobilliummovieapp.util.QUERY_PAGE_SIZE
import com.gllce.mobilliummovieapp.util.Resource
import com.gllce.mobilliummovieapp.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_fragment.*


@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private val upComingAdapter = UpComingAdapter()
    private val nowPlayingSliderAdapter = NowPlayingSliderAdapter(arrayListOf())

    var isLastPage = false
    var isLoading = false
    var isScrolling = false

    /*private val nestedScrollListener =
        NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->

            val layoutManager = upComingRecyclerView.layoutManager as LinearLayoutManager

            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.refreshData(false)
                isScrolling = false
            }
        }*/

    private val recyclerViewScrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager

            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.refreshData(false)
                isScrolling = false
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        upComingRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = upComingAdapter
            addOnScrollListener(this@MainFragment.recyclerViewScrollListener)
        }

        nowPlayingSlider.apply {
            setSliderAdapter(nowPlayingSliderAdapter)
            isNestedScrollingEnabled = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                    swipeRefreshLayout.isEnabled = scrollY == oldScrollY
                }
            }
        }

//        nestedScrollView.setOnScrollChangeListener(this@MainFragment.nestedScrollListener)

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshData(true)
            upComingRecyclerView.visibility = View.GONE
        }

        observeLiveData()
    }

    private fun hideProgressBar() {
        upComingLoading.visibility = View.INVISIBLE
        swipeRefreshLayout.isRefreshing = false
        isLoading = false
    }

    private fun showProgressBar() {
        upComingLoading.visibility = View.VISIBLE
        isLoading = true
    }

    private fun observeLiveData() {
        viewModel.upComingMovies.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    println("Success")
                    response.data?.let { upComing ->
                        val list = upComing.results
                        upComingAdapter.submitList(list)
                        upComingRecyclerView.visibility = View.VISIBLE
                        val totalPages = upComing.total_results / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.upComingPage == totalPages
                        println("upComingMovies observeLiveData")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                    println("Loading")
                }
                is Resource.Error -> {
                    hideProgressBar()
                    println("Error")
                    response.message?.let { message ->
                        Toast.makeText(context, "An error occurred: $message", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        })

        viewModel.nowPlayingMovies.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    //hideProgressBar()
                    println("Success")
                    response.data?.let { nowPlaying ->
                        val list = nowPlaying.results
                        nowPlayingSliderAdapter.updateList(list)
                        //set adapter
                        println("nowPlaying observeLiveData")
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