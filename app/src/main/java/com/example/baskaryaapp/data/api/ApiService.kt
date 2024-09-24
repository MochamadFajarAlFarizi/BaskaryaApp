package com.example.baskaryaapp.data.api

import com.example.baskaryaapp.data.response.ArticlesResponse
import com.example.baskaryaapp.data.response.BatikResponse
import com.example.baskaryaapp.data.response.GenerateResponse
import com.example.baskaryaapp.data.response.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    @GET("/api/articles")
    suspend fun article(
    ): ArticlesResponse

    @GET("/api/batik")
    suspend fun batik(
    ): BatikResponse

    @GET("api/search")
    fun searchbatik(
        @Query("batik") query: String,
    ): Call<BatikResponse>

    @GET("api/search")
    fun searcharticle(
        @Query("article") query: String,
    ): Call<ArticlesResponse>

    @Multipart
    @POST("api/ml")
    fun postbatik(
        @Part file: MultipartBody.Part
    ): Call<UploadResponse>


    @POST("/api/custom")
    @FormUrlEncoded
    fun customBatik(
        @Field("uid") username: String,
        @Field("imageUrl") imageUrl: String,
        @Field("batikName") batikName: String
    ): Call<GenerateResponse>

    @POST("/api/custom")
    @FormUrlEncoded
    fun custom(
    ): GenerateResponse

}