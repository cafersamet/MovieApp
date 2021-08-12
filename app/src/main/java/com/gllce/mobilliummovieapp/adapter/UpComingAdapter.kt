package com.gllce.mobilliummovieapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.gllce.mobilliummovieapp.R
import com.gllce.mobilliummovieapp.databinding.UpComingItemBinding
import com.gllce.mobilliummovieapp.model.Movie

class UpComingAdapter(private val upComingMovies: ArrayList<Movie>) :
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
        holder.view.movie = upComingMovies[position]
        holder.view.clickListener = this
    }

    fun updateList(newList: List<Movie>) {
        upComingMovies.clear()
        upComingMovies.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return upComingMovies.size
    }

    override fun onItemClicked(v: View) {
        println("item clicked")
        /* val uuid = v.countryUuidText.text.toString().toInt();
         val action = FeedFragmentDirections.actionFeedFragment2ToCountryFragment2(uuid)
         Navigation.findNavController(v).navigate(action)*/
    }
}