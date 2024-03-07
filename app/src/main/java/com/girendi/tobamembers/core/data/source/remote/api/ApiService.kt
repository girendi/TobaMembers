package com.girendi.tobamembers.core.data.source.remote.api

import com.girendi.tobamembers.core.data.source.remote.response.PhotoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("photos")
    suspend fun fetchPhotos(
        @Query("_page") page: Int,
        @Query("_limit") limit: Int
    ): Response<List<PhotoResponse>>
}