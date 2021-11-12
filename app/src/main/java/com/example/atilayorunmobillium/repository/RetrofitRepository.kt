package com.example.atilayorunmobillium.repository

import com.example.atilayorunmobillium.api.ApiService
import javax.inject.Inject

class RetrofitRepository @Inject constructor(private val apiService: ApiService)  {
    suspend fun getMovieNowPlaying(authorization: String) = apiService.getMovieNowPlaying(authorization)
}