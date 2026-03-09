package com.example.iftardroid.api

import com.example.iftardroid.model.AladhanResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AladhanApi {
    @GET("timingsByCity")
    suspend fun getPrayerTimes(
        @Query("city") city: String,
        @Query("country") country: String = "Turkey",
        @Query("method") method: Int = 13 // Diyanet İşleri Başkanlığı
    ): Response<AladhanResponse>
}
