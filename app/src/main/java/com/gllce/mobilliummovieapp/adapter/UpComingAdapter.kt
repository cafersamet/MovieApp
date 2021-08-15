package com.gllce.mobilliummovieapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gllce.mobilliummovieapp.databinding.UpComingItemBinding
import com.gllce.mobilliummovieapp.model.Movie
import com.gllce.mobilliummovieapp.view.MainFragmentDirections

class UpComingAdapter :
    PagingDataAdapter<Movie, UpComingAdapter.UpComingViewHolder>(COMPARATOR) {

    class UpComingViewHolder(private val binding: UpComingItemBinding) :
        RecyclerView.ViewHolder(binding.root), ItemClickListener {

        fun bind(item: Movie?) {
            binding.movie = item
            binding.clickListener = this
        }

        companion object {
            fun create(view: ViewGroup): UpComingViewHolder {
                val inflater = LayoutInflater.from(view.context)
                val binding = UpComingItemBinding.inflate(inflater, view, false)
                return UpComingViewHolder(binding)
            }
        }

        override fun onItemClicked(v: View, id: String) {
            println("item clicked upcoming")
            val action = MainFragmentDirections.actionMainFragmentToDetailFragment(id.toInt())
            Navigation.findNavController(v).navigate(action)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpComingViewHolder {
        return UpComingViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: UpComingViewHolder, position: Int) {
        val movie = getItem(position)
        if (movie != null) {
            holder.bind(movie)
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }

}