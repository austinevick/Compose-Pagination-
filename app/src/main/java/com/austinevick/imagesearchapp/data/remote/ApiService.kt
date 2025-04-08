package com.austinevick.imagesearchapp.data.remote

import com.austinevick.imagesearchapp.data.model.ImageResponseModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("api/")
    suspend fun getImages(
        @Query("key") apiKey: String = "14864619-923ee1204c9967979dc2d22bf",
        @Query("q") query: String,
        @Query("page") page: Int
    ): ImageResponseModel
}