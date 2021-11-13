package com.example.atilayorunmobillium.data.dataStore

import androidx.paging.PagingSource
import com.example.atilayorunmobillium.data.api.ApiService
import com.example.atilayorunmobillium.data.model.Results
import com.example.atilayorunmobillium.data.repository.RetrofitRepository
import javax.inject.Inject

class MoviesPagingSource @Inject constructor(private val retrofitRepository: RetrofitRepository): PagingSource<Int, Results>(){
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Results> {
        val position = params.key ?: STARTING_INDEX
        return try {
            println(position)
            val movies = retrofitRepository.getMovieUpcoming(ApiService.authorization,position)
            LoadResult.Page(
                data = movies.body()!!.results,
                prevKey = if (position == STARTING_INDEX) null else position - 1,
                nextKey = if (movies.body()!!.results.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        const val STARTING_INDEX = 1
    }
}