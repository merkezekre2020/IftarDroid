package com.example.iftardroid.model

import com.google.gson.annotations.SerializedName

data class AladhanResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: AladhanData
)

data class AladhanData(
    @SerializedName("timings") val timings: PrayerTimings,
    @SerializedName("date") val date: PrayerDate
)

data class PrayerTimings(
    @SerializedName("Fajr") val fajr: String,
    @SerializedName("Sunrise") val sunrise: String,
    @SerializedName("Dhuhr") val dhuhr: String,
    @SerializedName("Asr") val asr: String,
    @SerializedName("Maghrib") val maghrib: String,
    @SerializedName("Isha") val isha: String
)

data class PrayerDate(
    @SerializedName("readable") val readable: String,
    @SerializedName("timestamp") val timestamp: String
)
