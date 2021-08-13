package com.gllce.mobilliummovieapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.gllce.mobilliummovieapp.databinding.NowPlayingItemBinding
import com.gllce.mobilliummovieapp.model.Movie
import com.gllce.mobilliummovieapp.view.MainFragmentDirections
import com.smarteist.autoimageslider.SliderViewAdapter


class NowPlayingSliderAdapter(private val nowPlayingMovies: ArrayList<Movie>) :
    SliderViewAdapter<NowPlayingSliderAdapter.NowPlayingViewHolder>(), ItemClickListener {

    class NowPlayingViewHolder(var view: NowPlayingItemBinding) :
        SliderViewAdapter.ViewHolder(view.root)

    fun updateList(list: MutableList<Movie>) {
        nowPlayingMovies.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup): NowPlayingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<NowPlayingItemBinding>(
            inflater,
            com.gllce.mobilliummovieapp.R.layout.now_playing_item,
            parent,
            false
        )
        return NowPlayingViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: NowPlayingViewHolder, position: Int) {
        viewHolder.view.movie = nowPlayingMovies[position]
        viewHolder.view.clickListener = this
    }

    override fun getCount(): Int {
        return nowPlayingMovies.size
    }

    override fun onItemClicked(v: View, id: Int) {
        println("item clicked now playing")
        val action = MainFragmentDirections.actionMainFragmentToDetailFragment(id)
        Navigation.findNavController(v).navigate(action)
    }
}