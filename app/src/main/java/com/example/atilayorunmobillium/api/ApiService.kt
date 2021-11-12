package com.example.atilayorunmobillium.api

import com.example.atilayorunmobillium.model.Movies
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {
    companion object{
        val authorization = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1MTY5NTJlMDAzNDc5ZWFmZmEzYzJiZjI5ODlmYWNhMiIsInN1YiI6IjYxOGUyN2E0Y2I2ZGI1MDA4ZDY0OTgxZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.FjuN3ln2QI5lH1_rFVh1FMfq5U6pfsWDs1wSFjYwqyU"
    }

    @GET("movie/now_playing")
    suspend fun getMovieNowPlaying(
        @Header("Authorization") authorization: String
    ): Response<Movies>
}