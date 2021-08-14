package com.gllce.mobilliummovieapp.view

import android.os.Build
import android.os.Bundle
import android.util.Log
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
import kotlinx.android.synthetic.main.error_layout.view.*
import kotlinx.android.synthetic.main.main_fragment.*


@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private val upComingAdapter = UpComingAdapter()
    private val nowPlayingSliderAdapter = NowPlayingSliderAdapter(arrayListOf())
    private val TAG = "MainFragment"

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
                viewModel.getUpComingMovies()
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
        initViews()
        observeLiveData()
    }

    private fun initViews() {
        initRecyclerView()
        setSlider()
//        nestedScrollView.setOnScrollChangeListener(this@MainFragment.nestedScrollListener)
        setSwipeRefreshLayout()

        upComingErrorLayout.retry_button.setOnClickListener {
            viewModel.getUpComingMovies()
        }
        nowPlayingErrorLayout.retry_button.setOnClickListener {
            viewModel.getNowPlayingMovies()
        }
    }

    private fun initRecyclerView() {
        upComingRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = upComingAdapter
            addOnScrollListener(this@MainFragment.recyclerViewScrollListener)
        }
    }

    private fun setSlider() {
        nowPlayingSlider.apply {
            setSliderAdapter(nowPlayingSliderAdapter)
            isNestedScrollingEnabled = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                    swipeRefreshLayout.isEnabled = scrollY == oldScrollY
                }
            }
        }
    }

    private fun setSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshData(true)
            upComingRecyclerView.visibility = View.GONE
        }
    }

    private fun showProgressBar() {
        showUpComingLoadingView()
        isLoading = true
    }

    private fun observeLiveData() {
        viewModel.upComingMovies.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    hideUpComingAllViews()
                    Log.i(TAG, "Success")
                    response.data?.let { upComing ->
                        val list = upComing.results
                        upComingAdapter.submitList(list)
                        upComingRecyclerView.visibility = View.VISIBLE
                        val totalPages = upComing.total_results / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.upComingPage == totalPages
                        Log.i(TAG, "upComingMovies observeLiveData")
                    }

                    if (upComingAdapter.itemCount == 0) {
                        showUpComingEmptyView()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                    Log.i(TAG, "Loading")
                }
                is Resource.Error -> {
                    showUpComingErrorView()
                    Log.i(TAG, "Error")
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
                    hideNowPlayingAllViews()
                    Log.i(TAG, "Success")
                    response.data?.let { nowPlaying ->
                        val list = nowPlaying.results
                        nowPlayingSliderAdapter.updateList(list)
                        //set adapter
                        Log.i(TAG, "nowPlaying observeLiveData")
                    }
                    if (nowPlayingSliderAdapter.count == 0) {
                        showNowPlayingEmptyView()
                    }
                }
                is Resource.Loading -> {
                    Log.i(TAG, "nowPlaying Loading")
                    showNowPlayingLoadingView()
                }
                is Resource.Error -> {
                    Log.i(TAG, "nowPlaying Error")
                    showNowPlayingErrorView()
                    response.message?.let { message ->
                        Toast.makeText(context, "An error occurred: $message", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        })
    }

    private fun showUpComingEmptyView() {
        upComingLoadingLayout.visibility = View.GONE
        upComingErrorLayout.visibility = View.GONE

        upComingRecyclerView.visibility = View.GONE
        upComingEmptyLayout.visibility = View.VISIBLE

        swipeRefreshLayout.isRefreshing = false
        isLoading = false
    }

    private fun showUpComingErrorView() {
        upComingLoadingLayout.visibility = View.GONE
        upComingEmptyLayout.visibility = View.GONE

        upComingRecyclerView.visibility = View.GONE
        upComingErrorLayout.visibility = View.VISIBLE


        swipeRefreshLayout.isRefreshing = false
        isLoading = false
    }

    private fun showUpComingLoadingView() {
        upComingEmptyLayout.visibility = View.GONE
        upComingErrorLayout.visibility = View.GONE

        upComingLoadingLayout.visibility = View.VISIBLE
    }

    private fun hideUpComingAllViews() {
        upComingLoadingLayout.visibility = View.GONE
        upComingErrorLayout.visibility = View.GONE
        upComingEmptyLayout.visibility = View.GONE

        upComingRecyclerView.visibility = View.VISIBLE

        swipeRefreshLayout.isRefreshing = false
        isLoading = false
    }

    private fun showNowPlayingEmptyView() {
        nowPlayingLoadingLayout.visibility = View.GONE
        nowPlayingErrorLayout.visibility = View.GONE
        nowPlayingEmptyLayout.visibility = View.VISIBLE
    }

    private fun showNowPlayingErrorView() {
        nowPlayingLoadingLayout.visibility = View.GONE
        nowPlayingEmptyLayout.visibility = View.GONE
        nowPlayingErrorLayout.visibility = View.VISIBLE
    }

    private fun showNowPlayingLoadingView() {
        nowPlayingEmptyLayout.visibility = View.GONE
        nowPlayingErrorLayout.visibility = View.GONE
        nowPlayingLoadingLayout.visibility = View.VISIBLE
    }

    private fun hideNowPlayingAllViews() {
        nowPlayingLoadingLayout.visibility = View.GONE
        nowPlayingErrorLayout.visibility = View.GONE
        nowPlayingEmptyLayout.visibility = View.GONE
    }
}