package com.example.iftardroid.model

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: T
)

data class City(
    @SerializedName("name") val name: String,
    @SerializedName("population") val population: Int,
    @SerializedName("districts") val districts: List<District>
)

data class District(
    @SerializedName("name") val name: String,
    @SerializedName("population") val population: Int
)
