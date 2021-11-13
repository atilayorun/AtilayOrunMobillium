package com.example.atilayorunmobillium.api

import com.example.atilayorunmobillium.model.MovieDetail
import com.example.atilayorunmobillium.model.Movies
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    companion object{
        val authorization = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1MTY5NTJlMDAzNDc5ZWFmZmEzYzJiZjI5ODlmYWNhMiIsInN1YiI6IjYxOGUyN2E0Y2I2ZGI1MDA4ZDY0OTgxZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.FjuN3ln2QI5lH1_rFVh1FMfq5U6pfsWDs1wSFjYwqyU"
        val photoUrl = "https://www.themoviedb.org/t/p/w220_and_h330_face/"
    }

    @GET("movie/now_playing")
    suspend fun getMovieNowPlaying(
        @Header("Authorization") authorization: String
    ): Response<Movies>

    @GET("movie/upcoming")
    suspend fun getMovieUpcoming(
        @Header("Authorization") authorization: String,@Query("page") page:Int
    ): Response<Movies>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Header("Authorization") authorization: String,@Path("movie_id") movieId:Int
    ):Response<MovieDetail>
}