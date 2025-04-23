package com.example.whimsicalweather.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastResponse(
    val city: City,
    val list: List<DailyForecast>
)

@Serializable
data class City(
    val name: String
)

@Serializable
data class DailyForecast(
    val dt: Long,
    val temp: Temperature,
    val weather: List<WeatherDescription>
)

@Serializable
data class Temperature(
    val day: Double,
    val min: Double,
    val max: Double
)

@Serializable
data class WeatherDescription(
    val main: String, //use this for icons and screen change
    val description: String,
    val icon: String
)
