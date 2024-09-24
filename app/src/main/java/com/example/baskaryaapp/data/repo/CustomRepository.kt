package com.example.baskaryaapp.data.repo

import com.example.baskaryaapp.data.api.ApiService
import com.example.baskaryaapp.data.response.GenerateResponse

class CustomRepository private constructor(
    private val apiService: ApiService,
){
    suspend fun custom(): GenerateResponse {
        return apiService.custom()
    }

    companion object {
        @Volatile
        private var instance: CustomRepository? = null
        fun getInstance(
            apiService: ApiService
        ): CustomRepository =
            instance ?: synchronized(this) {
                instance ?: CustomRepository(apiService)
            }.also { instance = it }
    }
}