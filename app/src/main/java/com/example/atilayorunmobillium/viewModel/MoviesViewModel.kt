package com.example.atilayorunmobillium.viewModel

import android.content.res.Resources
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.atilayorunmobillium.R
import com.example.atilayorunmobillium.Util.NetworkResult
import com.example.atilayorunmobillium.api.ApiService
import com.example.atilayorunmobillium.dataStore.MoviesPagingSource
import com.example.atilayorunmobillium.model.Movies
import com.example.atilayorunmobillium.model.Results
import com.example.atilayorunmobillium.repository.RetrofitRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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

    fun getMovies(): Flow<PagingData<Results>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { MoviesPagingSource(retrofitRepository) }
        ).flow
            .cachedIn(viewModelScope)
    }
}