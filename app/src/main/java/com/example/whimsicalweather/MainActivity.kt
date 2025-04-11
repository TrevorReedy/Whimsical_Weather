package com.example.whimsicalweather
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.whimsicalweather.data.api.RetrofitInstance
import com.example.whimsicalweather.data.repository.WeatherRepository
import com.example.whimsicalweather.ui.screen.WeatherScreen
import com.example.whimsicalweather.ui.viewmodel.WeatherViewModel
import com.example.whimsicalweather.ui.theme.WhimsicalWeatherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = WeatherRepository(RetrofitInstance.weatherApiService)
        val viewModel = WeatherViewModel(repository)

        // Hardcoded latitude and longitude (replace with actual values)
        val lat =  44.95 // Example: St Paul
        val lon = 93.09 // Example: St Paul
//      val apiKey : String = BuildConfig.WEATHER_API_KEY //REPLACE WITH YOUR API KEY AS A STRING
        val apiKey = "061dcf52b110f6739037bf2bf8f472e7"
        // Fetch weather data
        viewModel.fetchWeather(lat, lon, apiKey)

        // Set the UI
        setContent {
            WhimsicalWeatherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherScreen(viewModel = viewModel)
                }
            }
        }
    }
}