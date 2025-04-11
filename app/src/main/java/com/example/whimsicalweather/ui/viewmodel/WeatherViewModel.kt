package com.example.whimsicalweather.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whimsicalweather.data.model.WeatherResponse
import com.example.whimsicalweather.data.repository.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    private val _temperature = MutableLiveData<Double>()
    val temperature: LiveData<Double> get() = _temperature

    private val _cityName = MutableLiveData<String>()
    val cityName: LiveData<String> get() = _cityName

    private val _lat = MutableLiveData<Double>()
    val lat: LiveData<Double> get() = _lat

    private val _lon = MutableLiveData<Double>()
    val lon: LiveData<Double> get() = _lon


    private val _tempMin = MutableLiveData<Double>()
    val tempMin: LiveData<Double> get() = _tempMin


    private val _tempMax = MutableLiveData<Double>()
    val tempMax: LiveData<Double> get() = _tempMax


    private val _humidity = MutableLiveData<Int>()
    val humidity: LiveData<Int> get() = _humidity

    private val _pressure = MutableLiveData<Int>()
    val pressure: LiveData<Int> get() = _pressure

    private val _feelsLike = MutableLiveData<Double>()
    val feelsLike: LiveData<Double> get() = _feelsLike


    fun fetchWeather(lat: Double, lon: Double, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = repository.getWeatherData(lat, lon, apiKey)
                Log.e("WeatherViewModel", "API Response: $response") // Log the response
                _temperature.value = response.main.temp
                _cityName.value = response.name

                _lat.value = response.coord.lat
                _lon.value = response.coord.lon

                _tempMin.value = response.main.tempMin
                _tempMax.value = response.main.tempMax

                _humidity.value = response.main.humidity
                _pressure.value = response.main.pressure

                _feelsLike.value = response.main.feelsLike



            } catch (e: Exception) {
                // Handle error (e.g., show a message)
                Log.e("WeatherViewModel", "Error fetching weather data: ${e.message}")
            }
        }
    }
}