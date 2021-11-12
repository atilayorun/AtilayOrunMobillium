package com.example.atilayorunmobillium.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.atilayorunmobillium.databinding.ItemMoviesUpcomingBinding
import com.example.atilayorunmobillium.model.Results

class MoviesUpcomingAdapter(listener:MoviesUpcomingAdapterListener) : RecyclerView.Adapter<MoviesUpcomingAdapter.ViewHolder>(){
    private var listResults: List<Results> = listOf()
    private val listener = listener

    interface MoviesUpcomingAdapterListener{
        fun itemOnClickListener(movieId:Int)
    }

    inner class ViewHolder(val binding: ItemMoviesUpcomingBinding) : RecyclerView.ViewHolder(binding.root) {
        val ivMoviePhoto = binding.ivMoviePhoto
        val tvMovieDate = binding.tvMovieDate
        val tvMovieDescription = binding.tvMovieDescription
        val tvMovieTitle = binding.tvMovieTitle
        val ivChevronRight = binding.ivChevronRight
        val clContainer= binding.clContainer
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesUpcomingAdapter.ViewHolder {
        val itemMoviesUpcomingBinding: ItemMoviesUpcomingBinding = ItemMoviesUpcomingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(itemMoviesUpcomingBinding)
    }

    override fun onBindViewHolder(holder: MoviesUpcomingAdapter.ViewHolder, position: Int) {
        val item = listResults[position]
        holder.binding.tvMovieDate.text = item.release_date
        holder.binding.tvMovieDescription.text = item.overview
        holder.binding.tvMovieTitle.text = item.title
        holder.binding.clContainer.setOnClickListener{
            listener.itemOnClickListener(item.id)
        }
        Glide.with(holder.binding.ivChevronRight)
            .load("https://www.themoviedb.org/t/p/w220_and_h330_face/${item.poster_path}")
            .into(holder.binding.ivChevronRight)
    }

    override fun getItemCount(): Int {
        return listResults.size
    }

    fun setData(listResults: List<Results>) {
        this.listResults = listResults
        notifyDataSetChanged()
    }
}