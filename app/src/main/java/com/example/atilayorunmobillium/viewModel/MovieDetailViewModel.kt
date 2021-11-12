package com.example.atilayorunmobillium.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.atilayorunmobillium.api.ApiService
import com.example.atilayorunmobillium.model.MovieDetail
import com.example.atilayorunmobillium.repository.RetrofitRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieDetailViewModel @ViewModelInject constructor(private val retrofitRepository: RetrofitRepository) :
    ViewModel() {
    val movieDetailLiveData = MutableLiveData<MovieDetail>()

    fun getMovieDetail(movieId:Int){
        CoroutineScope(Dispatchers.IO).launch {
            retrofitRepository.getMovieDetail(ApiService.authorization,movieId).let {
                if(it.isSuccessful){
                    movieDetailLiveData.postValue(it.body())
                }
            }
        }
    }
}