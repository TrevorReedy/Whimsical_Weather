package com.example.whimsicalweather.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.whimsicalweather.data.model.ForecastResponse
import com.example.whimsicalweather.data.repository.WeatherRepository
import kotlinx.coroutines.launch

class ForecastViewModel(private val repository: WeatherRepository): ViewModel() {

    private val _forecast = MutableLiveData<ForecastResponse>()
    val forecast: LiveData<ForecastResponse> get() = _forecast

    fun fetchForecast(zipCode: String, apiKey: String) {
        viewModelScope.launch {
            try {
                _forecast.value = repository.getForecast(zipCode, apiKey)
            } catch (e: Exception) {
                // Log or show error
            }
        }
    }
}
