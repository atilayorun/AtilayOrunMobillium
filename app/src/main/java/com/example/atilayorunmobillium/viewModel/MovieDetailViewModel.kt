package com.example.atilayorunmobillium.viewModel

import android.content.res.Resources
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.atilayorunmobillium.Util.NetworkResult
import com.example.atilayorunmobillium.api.ApiService
import com.example.atilayorunmobillium.model.MovieDetail
import com.example.atilayorunmobillium.repository.RetrofitRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.atilayorunmobillium.R
import com.example.atilayorunmobillium.Util.Util.loadImage

class MovieDetailViewModel @ViewModelInject constructor(private val retrofitRepository: RetrofitRepository) :
    ViewModel() {
    var response: MutableLiveData<NetworkResult<MovieDetail>> = MutableLiveData()

    fun getMovieDetail(movieId:Int){
        response.value = NetworkResult.Loading()
        CoroutineScope(Dispatchers.IO).launch {
            retrofitRepository.getMovieDetail(ApiService.authorization,movieId).let {
                try {
                    if (it.isSuccessful) {
                        response.postValue(NetworkResult.Success(it.body()!!))
                    } else {
                        response.postValue(NetworkResult.Error(
                            Resources.getSystem().getString(R.string.on_failure)
                        ))
                    }
                }catch (e: Exception){
                    response.postValue(NetworkResult.Error(e.message))
                }
            }
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("imgUrl")
        fun loadImage(view: ImageView, url: String?=null) {
            view.loadImage("https://www.themoviedb.org/t/p/w220_and_h330_face/$url")
        }
    }
}