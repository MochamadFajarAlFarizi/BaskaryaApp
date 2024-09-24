package com.example.baskaryaapp.data.repo

import com.example.baskaryaapp.data.api.ApiService
import com.example.baskaryaapp.data.response.BatikResponse

class BatikRepository private constructor(
    private val apiService: ApiService,
){
    suspend fun batik(): BatikResponse {
        return apiService.batik()
    }

    companion object {
        @Volatile
        private var instance: BatikRepository? = null
        fun getInstance(
            apiService: ApiService
        ): BatikRepository =
            instance ?: synchronized(this) {
                instance ?: BatikRepository(apiService)
            }.also { instance = it }
    }
}