package com.example.whimsicalweather.data.repository

import com.example.whimsicalweather.data.api.WeatherApiService
import com.example.whimsicalweather.data.model.WeatherResponse

class WeatherRepository(private val weatherApiService: WeatherApiService) {
    suspend fun getWeatherData(lat: Double, lon: Double, apiKey: String): WeatherResponse {
        return weatherApiService.getWeatherData(lat, lon, apiKey)
    }
}
