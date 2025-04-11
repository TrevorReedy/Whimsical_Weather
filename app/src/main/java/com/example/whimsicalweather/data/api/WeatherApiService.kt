package com.example.whimsicalweather.data.api

import com.example.whimsicalweather.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query
interface WeatherApiService {
    @GET("weather")
    suspend fun getWeatherData(
        @Query("lat") lat: Double, // Latitude
        @Query("lon") lon: Double, // Longitude
        @Query("appid") apiKey: String, // API key
        @Query("units") units: String = "imperial" // Optional: Units (metric, imperial, etc.)
    ): WeatherResponse
}