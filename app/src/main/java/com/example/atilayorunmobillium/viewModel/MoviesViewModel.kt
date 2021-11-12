package com.example.atilayorunmobillium.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.atilayorunmobillium.api.ApiService
import com.example.atilayorunmobillium.model.Movies
import com.example.atilayorunmobillium.repository.RetrofitRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoviesViewModel @ViewModelInject constructor(private val retrofitRepository: RetrofitRepository) :
    ViewModel() {
    val moviesLiveData = MutableLiveData<Movies>()

    fun getMovieNowPlaying() {
        CoroutineScope(Dispatchers.IO).launch {
            retrofitRepository.getMovieNowPlaying(ApiService.authorization).let {
                if(it.isSuccessful){
                    moviesLiveData.postValue(it.body())
                }
            }
        }
    }
}