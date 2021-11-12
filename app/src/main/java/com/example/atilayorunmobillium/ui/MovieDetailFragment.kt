package com.example.atilayorunmobillium.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.navArgs
import com.example.atilayorunmobillium.R
import com.example.atilayorunmobillium.databinding.FragmentMovieDetailBinding
import com.example.atilayorunmobillium.databinding.FragmentMoviesBinding
import com.example.atilayorunmobillium.viewModel.MovieDetailViewModel

class MovieDetailFragment : Fragment() {
    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel : MovieDetailViewModel by viewModels()
    private val args by navArgs<MovieDetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        viewModelTransactions()
        setObservers()
        return binding.root
    }

    private fun viewModelTransactions(){
        viewModel.getMovieDetail(args.movieId)
    }

    private fun setObservers() {
        viewModel.movieDetailLiveData.observe(viewLifecycleOwner, {
            binding.movieRate.text = it.vote_average.toString()
            binding.tvMovieDetailDate.text = it.release_date
            binding.tvMovieDetailTitle.text = it.title
            binding.tvMovieDetailDescription.text = it.overview
        })
    }
}