package com.gllce.mobilliummovieapp.view

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.gllce.mobilliummovieapp.adapter.MovieLoadStateAdapter
import com.gllce.mobilliummovieapp.adapter.NowPlayingSliderAdapter
import com.gllce.mobilliummovieapp.adapter.UpComingAdapter
import com.gllce.mobilliummovieapp.databinding.MainFragmentBinding
import com.gllce.mobilliummovieapp.util.Resource
import com.gllce.mobilliummovieapp.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    private val upComingAdapter by lazy { UpComingAdapter() }
    private val nowPlayingSliderAdapter = NowPlayingSliderAdapter(arrayListOf())
    private val TAG = "MainFragment"

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        viewModel.getNowPlayingMovies()
        observeLiveData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViews() {
        initRecyclerView()
        setSlider()
        setSwipeRefreshLayout()

        binding.nowPlayingErrorLayout.errorRetryButton.setOnClickListener {
            viewModel.getNowPlayingMovies()
        }
    }

    private fun initRecyclerView() {
        binding.upComingRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
        }
        binding.upComingRecyclerView.adapter = upComingAdapter.withLoadStateHeaderAndFooter(
            header = MovieLoadStateAdapter { upComingAdapter.retry() },
            footer = MovieLoadStateAdapter { upComingAdapter.retry() }
        )

        upComingAdapter.addLoadStateListener { loadState ->
            binding.upComingRecyclerView.isVisible =
                loadState.source.refresh is LoadState.NotLoading
            binding.upComingProgressBar.isVisible = loadState.source.refresh is LoadState.Loading
            binding.upComingRetryButton.isVisible = loadState.source.refresh is LoadState.Error
            binding.upComingErrorText.isVisible = loadState.source.refresh is LoadState.Error
            handleError(loadState)
        }

        binding.upComingRetryButton.setOnClickListener {
            upComingAdapter.retry()
        }
    }

    private fun handleError(loadState: CombinedLoadStates) {
        val errorState = loadState.source.append as? LoadState.Error
            ?: loadState.source.prepend as? LoadState.Error
        errorState?.let {
            Toast.makeText(context, "${it.error})", Toast.LENGTH_LONG).show()
        }
    }

    private fun setSlider() {
        binding.nowPlayingSlider.apply {
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
        binding.swipeRefreshLayout.setOnRefreshListener {
            upComingAdapter.refresh()
            viewModel.getNowPlayingMovies()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun observeLiveData() {
        lifecycleScope.launch {
            viewModel.upComingPagingList.collectLatest {
                upComingAdapter.submitData(it)
            }
        }

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