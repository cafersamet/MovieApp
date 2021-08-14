package com.gllce.mobilliummovieapp.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.gllce.mobilliummovieapp.R
import com.gllce.mobilliummovieapp.adapter.ItemClickListener
import com.gllce.mobilliummovieapp.databinding.DetailFragmentBinding
import com.gllce.mobilliummovieapp.util.Resource
import com.gllce.mobilliummovieapp.viewModel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.detail_fragment.*
import kotlinx.android.synthetic.main.error_layout.view.*

@AndroidEntryPoint
class DetailFragment : Fragment(), ItemClickListener {
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
        dataBinding.clickListener = this
        viewModel.getMovieDetail(movieId)
        observeLiveData()

        detailErrorLayout.retry_button.setOnClickListener {
            viewModel.getMovieDetail(movieId)
        }
    }

    private fun observeLiveData() {
        viewModel.movieDetail.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    hideAllViews()
                    println("Success")
                    response.data?.let { movie ->
                        println("detail observeLiveData")
                        dataBinding.movie = movie
                    }
                    if (dataBinding.movie == null) {
                        showEmptyView()
                    }
                }
                is Resource.Loading -> {
                    showLoadingView()
                    println("detail Loading")
                }
                is Resource.Error -> {
                    showErrorView()
                    println("detail Error")
                    response.message?.let { message ->
                        Toast.makeText(context, "An error occurred: $message", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }

        })
    }

    override fun onItemClicked(v: View, id: String) {
        openNewTabWindow(id)
    }

    private fun openNewTabWindow(imdbId: String) {
        val uris = Uri.parse("https://www.imdb.com/title/$imdbId")
        val intents = Intent(Intent.ACTION_VIEW, uris)
        val b = Bundle()
        b.putBoolean("new_window", true)
        intents.putExtras(b)
        context?.startActivity(intents)
    }

    private fun showEmptyView() {
        detailLoadingLayout.visibility = View.GONE
        detailErrorLayout.visibility = View.GONE
        detailEmptyLayout.visibility = View.VISIBLE
        detailScrollView.visibility = View.GONE
    }

    private fun showErrorView() {
        detailLoadingLayout.visibility = View.GONE
        detailEmptyLayout.visibility = View.GONE
        detailErrorLayout.visibility = View.VISIBLE
        detailScrollView.visibility = View.GONE
    }

    private fun showLoadingView() {
        detailEmptyLayout.visibility = View.GONE
        detailErrorLayout.visibility = View.GONE
        detailLoadingLayout.visibility = View.VISIBLE
    }

    private fun hideAllViews() {
        detailLoadingLayout.visibility = View.GONE
        detailErrorLayout.visibility = View.GONE
        detailEmptyLayout.visibility = View.GONE
        detailScrollView.visibility = View.VISIBLE
    }

}