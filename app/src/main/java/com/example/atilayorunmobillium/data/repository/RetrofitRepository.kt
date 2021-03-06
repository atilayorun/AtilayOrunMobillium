package com.example.atilayorunmobillium.data.repository

import com.example.atilayorunmobillium.data.api.ApiService
import com.example.atilayorunmobillium.data.model.Movies
import javax.inject.Inject

class RetrofitRepository @Inject constructor(private val apiService: ApiService)  {
    suspend fun getMovieNowPlaying(authorization: String) = apiService.getMovieNowPlaying(authorization)
    suspend fun getMovieUpcoming(authorization: String,page:Int) = apiService.getMovieUpcoming(authorization,page)
    suspend fun getMovieDetail(authorization: String,movieId:Int) = apiService.getMovieDetail(authorization,movieId)
}