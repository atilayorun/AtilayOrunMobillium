package com.example.atilayorunmobillium.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.atilayorunmobillium.BR
import com.example.atilayorunmobillium.Util.NetworkResult
import com.example.atilayorunmobillium.databinding.FragmentMovieDetailBinding
import com.example.atilayorunmobillium.ui.viewModels.MovieDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {
    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MovieDetailViewModel by viewModels()
    private val args by navArgs<MovieDetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        setBinding()
        viewModelTransactions()
        setObservers()
        return binding.root
    }

    private fun viewModelTransactions() {
        viewModel.getMovieDetail(args.movieId)
    }

    private fun setBinding(){
        binding.lifecycleOwner = this
        binding.setVariable(BR.viewModel,viewModel)
    }

    private fun setObservers() {
        viewModel.response.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    binding.pbMovieDetail.visibility = View.GONE
                }
                is NetworkResult.Error -> {
                    binding.pbMovieDetail.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}