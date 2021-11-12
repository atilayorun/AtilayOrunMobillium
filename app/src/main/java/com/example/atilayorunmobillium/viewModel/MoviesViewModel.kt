package com.example.atilayorunmobillium.viewModel

import android.content.res.Resources
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.atilayorunmobillium.R
import com.example.atilayorunmobillium.Util.NetworkResult
import com.example.atilayorunmobillium.api.ApiService
import com.example.atilayorunmobillium.model.Movies
import com.example.atilayorunmobillium.repository.RetrofitRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class MoviesViewModel @ViewModelInject constructor(private val retrofitRepository: RetrofitRepository) :
    ViewModel() {
    var responseNowPlaying: MutableLiveData<NetworkResult<Movies>> = MutableLiveData()
    var responseUpcoming: MutableLiveData<NetworkResult<Movies>> = MutableLiveData()


    fun getMovieNowPlaying() {
        responseNowPlaying.value = NetworkResult.Loading()
        CoroutineScope(Dispatchers.IO).launch {
            retrofitRepository.getMovieNowPlaying(ApiService.authorization).let {
                try {
                    if (it.isSuccessful) {
                        responseNowPlaying.postValue(NetworkResult.Success(it.body()!!))
                    } else {
                        responseNowPlaying.postValue(NetworkResult.Error(Resources.getSystem().getString(R.string.on_failure)))
                    }
                } catch (e: Exception) {
                    responseNowPlaying.postValue(NetworkResult.Error(e.message))
                }
            }
        }
    }

    fun getMovieUpcoming() {
        responseUpcoming.value = NetworkResult.Loading()
        CoroutineScope(Dispatchers.IO).launch {
            retrofitRepository.getMovieUpcoming(ApiService.authorization, 1).let {
                try {
                    if (it.isSuccessful) {
                        responseUpcoming.postValue(NetworkResult.Success(it.body()!!))
                    } else {
                        responseUpcoming.postValue(NetworkResult.Error(Resources.getSystem().getString(R.string.on_failure)))
                    }
                } catch (e: Exception) {
                    responseUpcoming.postValue(NetworkResult.Error(e.message))
                }
            }
        }
    }
}