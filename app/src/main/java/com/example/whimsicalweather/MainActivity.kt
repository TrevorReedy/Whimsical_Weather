package com.example.whimsicalweather

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.example.whimsicalweather.data.api.RetrofitInstance
import com.example.whimsicalweather.data.repository.WeatherRepository
import com.example.whimsicalweather.service.LocationService
import com.example.whimsicalweather.ui.screen.ForecastScreen
import com.example.whimsicalweather.ui.screen.WeatherScreen
import com.example.whimsicalweather.ui.screen.getIcon
import com.example.whimsicalweather.ui.screen.getWeatherBackground
import com.example.whimsicalweather.ui.theme.WhimsicalWeatherTheme
import com.example.whimsicalweather.ui.viewmodel.ForecastViewModel
import com.example.whimsicalweather.ui.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var forecastViewModel: ForecastViewModel

    private lateinit var locationReceiver: BroadcastReceiver

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            Log.d("MainActivity", "COARSE location granted by user")
            weatherViewModel.enableLocationTracking(this)
            requestNotificationPermission()
        } else {
            Log.d("MainActivity", "COARSE location denied by user")
            weatherViewModel.disableLocationTracking(this)
            showAlertDialog("Location Denied", "Cannot show your weather without location access.")
        }
    }

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = WeatherRepository(RetrofitInstance.weatherApiService)
        weatherViewModel = WeatherViewModel(repository)
        forecastViewModel = ForecastViewModel(repository)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        createNotificationChannel()

        weatherViewModel.fetchWeather(44.95, -93.09, BuildConfig.API_KEY)

        locationReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val lat = intent?.getDoubleExtra("latitude", 0.0) ?: 0.0
                val lon = intent?.getDoubleExtra("longitude", 0.0) ?: 0.0
                Log.d("MainActivity", "Received broadcast intent")

                if (lat != 0.0 && lon != 0.0) {
                    Log.d("MainActivity", "Broadcast received: $lat, $lon")
                    weatherViewModel.fetchWeather(lat, lon, BuildConfig.API_KEY)
                    Log.w("MainActivity", "Broadcast received but coordinates missing")

                }
            }
        }

        ContextCompat.registerReceiver(
            this,
            locationReceiver,
            IntentFilter("com.example.whimsicalweather.LOCATION_UPDATE"),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )

        setContent {
            var showForecast by remember { mutableStateOf(false) }
            var currentZip by remember { mutableStateOf("55101") }

            val iconCode by weatherViewModel.icon.observeAsState("01d")
            val backGround = getWeatherBackground(iconCode)

            WhimsicalWeatherTheme {
                if (showForecast) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(id = backGround),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        ForecastScreen(
                            viewModel = forecastViewModel,
                            onBackClick = { showForecast = false }
                        )
                    }
                } else {
                    Surface(modifier = Modifier.fillMaxSize(), color = Color.Green) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Image(
                                painter = painterResource(id = backGround),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )

                            WeatherScreen(
                                viewModel = weatherViewModel,
                                onForecastClick = {
                                    forecastViewModel.fetchForecast(currentZip, BuildConfig.API_KEY)
                                    showForecast = true
                                },
                                onZipSearch = { zip ->
                                    currentZip = zip
                                    weatherViewModel.fetchWeatherByZip(zip, BuildConfig.API_KEY)
                                    forecastViewModel.fetchForecast(zip, BuildConfig.API_KEY)
                                },
                                onRequestLocation = { requestLocationPermission() }
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(locationReceiver)
    }

    private fun requestLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.d("MainActivity", "COARSE location already granted")
                weatherViewModel.enableLocationTracking(this)
                requestNotificationPermission()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) -> {
                showAlertDialog(
                    "Location Required",
                    "We need your location to show weather in your area."
                )
            }
            else -> {
                Log.d("MainActivity", "Requesting coarse location permission")
                locationPermissionLauncher.launch(
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
                )
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notification_channel_name)
            val descriptionText = getString(R.string.notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("weather_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showAlertDialog(title: String, message: String) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    @Composable
    fun BackgroundImageScreen() {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.lofi_day),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}
