package com.gllce.mobilliummovieapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gllce.mobilliummovieapp.R
import com.gllce.mobilliummovieapp.databinding.UpComingItemBinding
import com.gllce.mobilliummovieapp.model.Movie
import com.gllce.mobilliummovieapp.view.MainFragmentDirections

class UpComingAdapter :
    RecyclerView.Adapter<UpComingAdapter.UpComingViewHolder>(), ItemClickListener {

    class UpComingViewHolder(var view: UpComingItemBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpComingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<UpComingItemBinding>(
            inflater,
            R.layout.up_coming_item,
            parent,
            false
        )
        return UpComingViewHolder(view)
    }

    override fun onBindViewHolder(holder: UpComingViewHolder, position: Int) {
        holder.view.movie = differ.currentList[position]
        holder.view.clickListener = this
    }

    fun submitList(list: MutableList<Movie>) {
        val x = mutableListOf<Movie>().apply {
            addAll(list)
        }
        differ.submitList(x)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val differCallback = object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, differCallback)

    override fun onItemClicked(v: View, id: Int) {
        println("item clicked upcoming")
        val action = MainFragmentDirections.actionMainFragmentToDetailFragment(id)
        Navigation.findNavController(v).navigate(action)
    }
}