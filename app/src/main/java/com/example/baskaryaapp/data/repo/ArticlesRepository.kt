package com.example.baskaryaapp.data.repo

import com.example.baskaryaapp.data.api.ApiService
import com.example.baskaryaapp.data.response.ArticlesResponse

class ArticlesRepository private constructor(
    private val apiService: ApiService,
){
    suspend fun articles(): ArticlesResponse {
        return apiService.article()
    }

    companion object {
        @Volatile
        private var instance: ArticlesRepository? = null
        fun getInstance(
            apiService: ApiService
        ): ArticlesRepository =
            instance ?: synchronized(this) {
                instance ?: ArticlesRepository(apiService)
            }.also { instance = it }
    }
}