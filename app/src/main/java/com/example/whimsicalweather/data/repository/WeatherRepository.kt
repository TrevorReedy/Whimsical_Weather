package com.example.whimsicalweather.data.repository

import com.example.whimsicalweather.data.api.WeatherApiService
import com.example.whimsicalweather.data.model.ForecastResponse
import com.example.whimsicalweather.data.model.WeatherResponse

class WeatherRepository(private val weatherApiService: WeatherApiService) {
    suspend fun getWeatherData(lat: Double, lon: Double, apiKey: String): WeatherResponse {
        return weatherApiService.getWeatherData(lat, lon, apiKey)
    }


    suspend fun getForecast(zipCode: String, apiKey: String, days: Int = 7): ForecastResponse {
        return weatherApiService.getForecastData(zipCode, apiKey, days = days)
    }
}
