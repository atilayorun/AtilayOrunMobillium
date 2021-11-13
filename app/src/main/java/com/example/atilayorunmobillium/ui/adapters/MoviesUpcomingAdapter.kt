package com.example.atilayorunmobillium.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.atilayorunmobillium.Util.Util.loadImage
import com.example.atilayorunmobillium.data.api.ApiService
import com.example.atilayorunmobillium.databinding.ItemMoviesUpcomingBinding
import com.example.atilayorunmobillium.data.model.Results

class MoviesUpcomingAdapter(listener: MoviesUpcomingAdapterListener) : PagingDataAdapter<Results, MoviesUpcomingAdapter.ViewHolder>(
    ResultsDiffCallback
){
    private val listener = listener

    interface MoviesUpcomingAdapterListener{
        fun itemOnClickListener(movieId:Int)
    }

    inner class ViewHolder(val binding: ItemMoviesUpcomingBinding) : RecyclerView.ViewHolder(binding.root) {
        val ivMoviePhoto = binding.ivMoviePhoto
        val tvMovieDate = binding.tvMovieDate
        val tvMovieDescription = binding.tvMovieDescription
        val tvMovieTitle = binding.tvMovieTitle
        val clContainer= binding.clContainer
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemMoviesUpcomingBinding: ItemMoviesUpcomingBinding = ItemMoviesUpcomingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(itemMoviesUpcomingBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.tvMovieDate.text = item?.release_date
        holder.tvMovieDescription.text = item?.overview
        holder.tvMovieTitle.text = item?.title
        holder.clContainer.setOnClickListener{
            listener.itemOnClickListener(item!!.id)
        }
        holder.ivMoviePhoto.loadImage("${ApiService.photoUrl}${item?.poster_path}")
    }

    object ResultsDiffCallback: DiffUtil.ItemCallback<Results>() {
        override fun areItemsTheSame(oldItem: Results, newItem: Results): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Results, newItem: Results): Boolean {
            return oldItem == newItem
        }
    }
}