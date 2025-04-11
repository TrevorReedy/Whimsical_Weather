package com.example.whimsicalweather.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class WeatherResponse(
    val coord: Coord,
    val weather: List<Weather>,
    val main: Main,
    val name: String // City name
) {
    @Serializable
    data class Coord(
        val lon: Double,
        val lat: Double
    )

    @Serializable
    data class Weather(
        val id: Int,
        val main: String,
        val description: String,
        val icon: String
    )

    @Serializable
    data class Main(
        @SerialName("temp") val temp: Double,
        @SerialName("feels_like") val feelsLike: Double,
        @SerialName("temp_min") val tempMin: Double,
        @SerialName("temp_max") val tempMax: Double,
        @SerialName("pressure") val pressure: Int,
        @SerialName("humidity") val humidity: Int
    )

}
