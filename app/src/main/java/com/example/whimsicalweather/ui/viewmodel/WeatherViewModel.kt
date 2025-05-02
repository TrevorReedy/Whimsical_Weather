package com.example.whimsicalweather.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whimsicalweather.data.model.WeatherResponse
import com.example.whimsicalweather.data.repository.WeatherRepository
import com.example.whimsicalweather.service.LocationService
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

    private val _icon = MutableLiveData<String>()
    val icon: LiveData<String> get() = _icon

    private val _zipError = MutableLiveData<String?>()
    val zipError: LiveData<String?> get() = _zipError

    private val _zipCode = mutableStateOf("")
    val zipCode: State<String> get() = _zipCode

    private val _isLocationEnabled = MutableLiveData(false)
    val isLocationEnabled: LiveData<Boolean> get() = _isLocationEnabled

    fun enableLocationTracking(context: Context) {
        _isLocationEnabled.value = true
        Log.d("WeatherViewModel", "Enabling location tracking...")

        val intent = Intent(context, LocationService::class.java)
        val started = try {
            ContextCompat.startForegroundService(context, intent)
            true
        } catch (e: Exception) {
            Log.e("WeatherViewModel", "Failed to start service: ${e.message}")
            false
        }

        if (started) {
            Log.d("WeatherViewModel", "startForegroundService called successfully")
        }
    }


    fun disableLocationTracking(context: Context) {
        _isLocationEnabled.value = false
        val intent = Intent(context, LocationService::class.java)
        context.stopService(intent)
    }

    fun fetchWeather(lat: Double, lon: Double, apiKey: String) {
        Log.d("WeatherViewModel", "fetchWeather() called with $lat, $lon")

        viewModelScope.launch {
            try {
                val response = repository.getWeatherData(lat, lon, apiKey)
                Log.d("WeatherViewModel", "API Response: $response")
                updateWeatherData(response)
            } catch (e: retrofit2.HttpException) {
                Log.e("WeatherViewModel", "HTTP ${e.code()} ${e.message()}", e)
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("WeatherViewModel", "Error body: $errorBody")
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Generic failure: ${e.message}", e)
            }
        }
    }


    fun fetchWeatherByZip(zipCode: String, apiKey: String) {
        viewModelScope.launch {
            _zipError.value = null
            try {
                val response = repository.getWeatherByZip(zipCode, apiKey)
                updateWeatherData(response)
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error fetching by zip: ${e.message}")
                _zipError.value = "Invalid zip code or network error."
            }
        }
    }

    private fun updateWeatherData(response: WeatherResponse) {
        _temperature.value = response.main.temp
        _cityName.value = response.name
        _lat.value = response.coord.lat
        _lon.value = response.coord.lon
        _tempMin.value = response.main.tempMin
        _tempMax.value = response.main.tempMax
        _humidity.value = response.main.humidity
        _pressure.value = response.main.pressure
        _feelsLike.value = response.main.feelsLike
        _icon.value = response.weather.firstOrNull()?.icon ?: "01d"
        Log.d("WeatherViewModel", "API Response: $response")

    }

    fun clearZipError() {
        _zipError.value = null
    }

    fun updateZip(newZip: String) {
        _zipCode.value = newZip
    }
}