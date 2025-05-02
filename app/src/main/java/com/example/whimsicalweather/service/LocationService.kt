package com.example.whimsicalweather.service

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.viewModelScope
import com.example.whimsicalweather.BuildConfig
import com.example.whimsicalweather.MainActivity
import com.example.whimsicalweather.R
import com.example.whimsicalweather.data.api.RetrofitInstance
import com.example.whimsicalweather.data.model.WeatherResponse
import com.example.whimsicalweather.data.repository.WeatherRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val LOCATION_UPDATE_ACTION = "com.example.whimsicalweather.LOCATION_UPDATE"

class LocationService : LifecycleService() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val repository = WeatherRepository(RetrofitInstance.weatherApiService)

    override fun onCreate() {
        super.onCreate()
        Log.d("LocationService", "SERVICE CREATED")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val notification = createDefaultNotification()
        startForeground(NOTIFICATION_ID, notification)

        startLocationUpdates()
    }

    private fun createDefaultNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, "weather_channel")
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.getting_location))
            .setSmallIcon(R.drawable.sun)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 30000
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }

        if (hasLocationPermission()) {
            try {
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            } catch (securityException: SecurityException) {
                Log.e("LocationService", "Lost location permissions", securityException)
                stopSelf()
            }
        } else {
            stopSelf()
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.lastLocation?.let { location ->
                updateWeather(location)
            }
        }
    }

    private fun hasLocationPermission(): Boolean {
        val granted = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        Log.d("LocationService", "hasLocationPermission = $granted")
        return granted
    }

    private fun updateWeather(location: Location) {
        Log.d("LocationService", "Got location: ${location.latitude}, ${location.longitude}")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = repository.getWeatherData(
                    location.latitude,
                    location.longitude,
                    BuildConfig.API_KEY
                )
                updateNotification(response)
                sendLocationBroadcast(response)
            } catch (e: Exception) {
                Log.e("LocationService", "Error fetching weather", e)
            }
        }
    }

    private fun updateNotification(weather: WeatherResponse) {
        val notification = NotificationHelper.createWeatherNotification(this, weather)
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun sendLocationBroadcast(weather: WeatherResponse) {
        Log.d("LocationService", "Sending broadcast to app: ${weather.coord.lat}, ${weather.coord.lon}")
        val intent = Intent(LOCATION_UPDATE_ACTION).apply {
            putExtra("latitude", weather.coord.lat)
            putExtra("longitude", weather.coord.lon)
        }
        sendBroadcast(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    companion object {
        const val NOTIFICATION_ID = 1001
    }
}