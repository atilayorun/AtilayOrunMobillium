package com.example.atilayorunmobillium.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.example.atilayorunmobillium.R
import com.example.atilayorunmobillium.Util.NetworkResult
import com.example.atilayorunmobillium.Util.ProgressDialogManager
import com.example.atilayorunmobillium.ui.adapters.MoviesUpcomingAdapter
import com.example.atilayorunmobillium.ui.adapters.ViewPagerAdapter
import com.example.atilayorunmobillium.databinding.FragmentMoviesBinding
import com.example.atilayorunmobillium.data.model.Results
import com.example.atilayorunmobillium.ui.viewModels.MoviesViewModel
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment : Fragment(), MoviesUpcomingAdapter.MoviesUpcomingAdapterListener,
    ViewPagerAdapter.ViewPagerAdapterListener {
    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MoviesViewModel by viewModels()
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var adapter: MoviesUpcomingAdapter
    private var progressDialogManager: ProgressDialogManager? = null
    private var responseCount = 0
    private var currentIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        setupAdapter()
        setupViewPagerAdapter(listOf())
        viewModelTransactions()
        listeners()
        setCollectLatest()
        return binding.root
    }

    private fun setProgressDialog() {
        progressDialogManager = ProgressDialogManager().apply {
            this.showProgressDialog(
                requireActivity(),
                getString(R.string.loading),
                false
            )
        }
    }

    private fun listeners() {
        binding.appbarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            binding.swipe.isEnabled = verticalOffset == 0
        })

        binding.swipe.setOnRefreshListener {
            progressDialogManager?.showProgressDialog(
                requireActivity(),
                getString(R.string.loading),
                false
            )
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
                binding.swipe.isEnabled = false
            }

            override fun onPageSelected(position: Int) {
                updatePageIndicator(position)
            }
        })
    }

    private fun setupViewPagerAdapter(listResults: List<Results>) {
        viewPagerAdapter = ViewPagerAdapter(requireContext(), listResults, this)
        binding.viewPager.adapter = viewPagerAdapter
    }

    private fun viewModelTransactions() {
        viewModel.getMovieNowPlaying()
    }

    private fun setCollectLatest() {
        lifecycleScope.launch {
            viewModel.getMovies().collectLatest {
                if (binding.swipe.isRefreshing)
                    binding.swipe.isRefreshing = false
                adapter.submitData(it)
            }
        }
    }

    private fun setObservers() {
        viewModel.responseNowPlaying.observe(requireActivity(), { response ->
            when (response) {
                is NetworkResult.Success -> {
                    dismissProgressDialogAndSwipe()
                    response.data?.let {
                        addPageIndicators()
                        setupViewPagerAdapter(it.results)
                    }
                }
                is NetworkResult.Error -> {
                    dismissProgressDialogAndSwipe()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun dismissProgressDialogAndSwipe() {
        responseCount++
        if (binding.swipe.isRefreshing)
            binding.swipe.isRefreshing = false
        if (responseCount % 2 == 0)
            progressDialogManager?.dismissProgressDialog()
    }

    private fun setupAdapter() {
        adapter = MoviesUpcomingAdapter(this)
        binding.rvMoviesUpcoming.adapter = adapter
        binding.rvMoviesUpcoming.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        addLoadListener()
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
                println("active dot position : $position")
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

    override fun sliderItemOnClickListener(movieId: Int) {
        val action = MoviesFragmentDirections.actionNowPlayingFragmentToMovieDetailFragment(movieId)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.responseNowPlaying.removeObservers(this)
        _binding = null
    }

    private fun addLoadListener() {
        adapter.addLoadStateListener { combinedLoadStates ->
            when (val loadState = combinedLoadStates.source.append) {
                is LoadState.NotLoading -> {
                    progressDialogManager.let {
                        progressDialogManager?.dismissProgressDialog()
                    }
                }
                is LoadState.Loading -> {
                    setProgressDialog()
                }
                is LoadState.Error -> {
                    Toast.makeText(
                        requireContext(),
                        loadState.error.localizedMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}