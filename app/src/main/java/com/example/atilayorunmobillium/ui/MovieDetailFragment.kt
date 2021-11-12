package com.example.atilayorunmobillium.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.atilayorunmobillium.R
import com.example.atilayorunmobillium.Util.NetworkResult
import com.example.atilayorunmobillium.Util.ProgressDialogManager
import com.example.atilayorunmobillium.Util.Util.loadImage
import com.example.atilayorunmobillium.databinding.FragmentMovieDetailBinding
import com.example.atilayorunmobillium.model.MovieDetail
import com.example.atilayorunmobillium.viewModel.MovieDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {
    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MovieDetailViewModel by viewModels()
    private val args by navArgs<MovieDetailFragmentArgs>()
    private lateinit var progressDialogManager: ProgressDialogManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        viewModelTransactions()
        setObservers()
        return binding.root
    }

    private fun viewModelTransactions() {
        viewModel.getMovieDetail(args.movieId)
    }

    private fun setObservers() {
        viewModel.response.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    progressDialogManager.dismissProgressDialog()
                    response.data?.let {
                        setUi(it)
                    }
                }
                is NetworkResult.Error -> {
                    progressDialogManager.dismissProgressDialog()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Loading -> {
                    progressDialogManager = ProgressDialogManager().apply {
                        this.showProgressDialog(
                            requireActivity(), getString(R.string.loading), false
                        )
                    }
                }
            }
        }
    }

    private fun setUi(movieDetail: MovieDetail) {
        binding.movieRate.text = movieDetail.vote_average.toString()
        binding.tvMovieDetailDate.text = movieDetail.release_date
        binding.tvMovieDetailTitle.text = movieDetail.title
        binding.tvMovieDetailDescription.text = movieDetail.overview
        binding.ivMovieDetailPhoto.loadImage("https://www.themoviedb.org/t/p/w220_and_h330_face/${movieDetail.poster_path}")
    }
}