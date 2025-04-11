package com.example.whimsicalweather

import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType

import com.example.whimsicalweather.data.api.WeatherApiService // Correct import

object NetworkModule {
    private val json = Json {
        ignoreUnknownKeys = true // Ignore unknown JSON fields
        isLenient = true // Allow lenient parsing (optional)
    }

    private val contentType = "application/json".toMediaType()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/data/2.5/") // Base URL for OpenWeatherMap API
        .addConverterFactory(json.asConverterFactory(contentType)) // Use Kotlin Serialization converter
        .build()

    val weatherApiService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java) // Correct reference
    }
}