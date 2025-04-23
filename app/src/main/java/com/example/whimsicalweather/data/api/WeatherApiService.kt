package com.example.whimsicalweather.data.api

import com.example.whimsicalweather.data.model.ForecastResponse
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

    @GET("weather")
    suspend fun getWeatherByZip(
        @Query("zip") zipCode: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "imperial"
    ): WeatherResponse


    @GET("forecast/daily")
    suspend fun getForecastData(
        @Query("zip") zipCode: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "imperial",
        @Query("cnt") days: Int = 7 // how many days of forecast you want (e.g., 7, 10, 16)
    ): ForecastResponse

}