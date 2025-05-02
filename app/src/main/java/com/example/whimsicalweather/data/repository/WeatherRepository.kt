package com.example.whimsicalweather.data.repository

import android.util.Log
import com.example.whimsicalweather.data.api.WeatherApiService
import com.example.whimsicalweather.data.model.ForecastResponse
import com.example.whimsicalweather.data.model.WeatherResponse

class WeatherRepository(private val weatherApiService: WeatherApiService) {
    suspend fun getWeatherData(lat: Double, lon: Double, apiKey: String): WeatherResponse {
        Log.d("WeatherRepository", "Fetching weather at $lat, $lon")
        return weatherApiService.getWeatherData(lat, lon, apiKey)
    }


    suspend fun getForecast(zipCode: String, apiKey: String, days: Int = 7): ForecastResponse {
        return weatherApiService.getForecastData(zipCode, apiKey, days = days)
    }

    suspend fun getWeatherByZip(zipCode: String, apiKey: String): WeatherResponse {
        return weatherApiService.getWeatherByZip(zipCode, apiKey)
    }

}
