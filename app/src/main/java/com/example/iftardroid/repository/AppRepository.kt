package com.example.iftardroid.repository

import com.example.iftardroid.api.RetrofitClient
import com.example.iftardroid.model.AladhanResponse
import com.example.iftardroid.model.ApiResponse
import com.example.iftardroid.model.City
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository {

    suspend fun getCities(): Result<List<City>> = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.turkiyeApi.getCities()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.data)
            } else {
                Result.failure(Exception("Error fetching cities: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPrayerTimes(city: String): Result<AladhanResponse> = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.aladhanApi.getPrayerTimes(city = city)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error fetching prayer times: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
