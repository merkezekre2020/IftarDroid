package com.example.iftardroid.api

import com.example.iftardroid.model.ApiResponse
import com.example.iftardroid.model.City
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TurkiyeApi {
    @GET("provinces")
    suspend fun getCities(): Response<ApiResponse<List<City>>>
}
