package com.example.atilayorunmobillium.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.example.atilayorunmobillium.R
import com.example.atilayorunmobillium.Util.NetworkResult
import com.example.atilayorunmobillium.Util.ProgressDialogManager
import com.example.atilayorunmobillium.adapter.MoviesUpcomingAdapter
import com.example.atilayorunmobillium.adapter.ViewPagerAdapter
import com.example.atilayorunmobillium.databinding.FragmentMoviesBinding
import com.example.atilayorunmobillium.model.Results
import com.example.atilayorunmobillium.viewModel.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : Fragment(), MoviesUpcomingAdapter.MoviesUpcomingAdapterListener {
    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MoviesViewModel by viewModels()
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private var currentIndex: Int = 0
    private lateinit var adapter: MoviesUpcomingAdapter
    private lateinit var progressDialogManager: ProgressDialogManager
    private var responseCount = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)

        responseCount = 0
        setupAdapter()

        viewModelTransactions()
        setObservers()
        listeners()
        return binding.root
    }

    private fun listeners() {
        binding.swipe.setOnRefreshListener {
            viewModelTransactions()
        }
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                updatePageIndicator(position)
            }
        })
    }

    private fun setupViewPagerAdapter(listResults: List<Results>) {
        viewPagerAdapter = ViewPagerAdapter(requireContext(), listResults)
        binding.viewPager.adapter = viewPagerAdapter
    }

    private fun viewModelTransactions() {
        viewModel.getMovieNowPlaying()
        viewModel.getMovieUpcoming()
    }

    private fun setObservers() {
        viewModel.responseNowPlaying.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    dismissProgressDialog()
                    response.data?.let {
                        addPageIndicators()
                        setupViewPagerAdapter(it.results)
                    }
                }
                is NetworkResult.Error -> {
                    dismissProgressDialog()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Loading -> {
                    progressDialogManager = ProgressDialogManager().apply {
                        this.showProgressDialog(
                            requireActivity(),
                            getString(R.string.loading),
                            false
                        )
                    }
                }
            }
        })
        viewModel.responseUpcoming.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    dismissProgressDialog()
                    response.data?.let {
                        adapter.setData(it.results)
                    }
                }
                is NetworkResult.Error -> {
                    dismissProgressDialog()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun dismissProgressDialog() {
        responseCount++
        if (responseCount >= 2) {
            if (binding.swipe.isRefreshing)
                binding.swipe.isRefreshing = false
            progressDialogManager.dismissProgressDialog()
        }
    }

    private fun setupAdapter() {
        adapter = MoviesUpcomingAdapter(this)
        binding.rvMoviesUpcoming.adapter = adapter
        binding.rvMoviesUpcoming.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun addPageIndicators() {
        binding.lytPageIndicator.removeAllViews()
        for (i in 1..5) {
            val view = ImageView(context)
            view.setImageResource(R.drawable.non_active_dot)
            binding.lytPageIndicator.addView(view)
        }
        updatePageIndicator(currentIndex)
    }

    private fun updatePageIndicator(position: Int) {
        var imageView: ImageView
        val lp =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        lp.setMargins(8, 0, 8, 0)
        for (i in 0 until binding.lytPageIndicator.childCount) {
            imageView = binding.lytPageIndicator.getChildAt(i) as ImageView
            imageView.layoutParams = lp
            if (position == i) {
                imageView.setImageResource(R.drawable.active_dot)
            } else {
                imageView.setImageResource(R.drawable.non_active_dot)
            }
        }
    }

    override fun itemOnClickListener(movieId: Int) {
        val action = MoviesFragmentDirections.actionNowPlayingFragmentToMovieDetailFragment(movieId)
        findNavController().navigate(action)
    }
}