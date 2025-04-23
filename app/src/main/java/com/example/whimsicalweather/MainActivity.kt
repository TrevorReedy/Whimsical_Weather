package com.example.whimsicalweather
import android.media.Image
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.core.view.WindowCompat
import com.example.whimsicalweather.data.api.RetrofitInstance
import com.example.whimsicalweather.data.repository.WeatherRepository
import com.example.whimsicalweather.ui.screen.WeatherScreen
import com.example.whimsicalweather.ui.viewmodel.WeatherViewModel
import com.example.whimsicalweather.ui.theme.WhimsicalWeatherTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.whimsicalweather.ui.screen.ForecastScreen
import com.example.whimsicalweather.ui.screen.getIcon
import com.example.whimsicalweather.ui.screen.getWeatherBackground
import com.example.whimsicalweather.ui.viewmodel.ForecastViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = WeatherRepository(RetrofitInstance.weatherApiService)
        val weatherViewModel = WeatherViewModel(repository)
        val forecastViewModel = ForecastViewModel(repository)


        WindowCompat.setDecorFitsSystemWindows(window, false)
        // Hardcoded latitude and longitude (replace with actual values)
        val lat = 44.95 // Example: St Paul
        val lon = -93.09 // Example: St Paul



        val apiKey: String =
            BuildConfig.API_KEY //REPLACE WITH YOUR API KEY AS A STRING => "YOUR_API_KEY"
        // Fetch weather data
        weatherViewModel.fetchWeather(lat, lon, apiKey)




        setContent {
            var showForecast by remember { mutableStateOf(false) }
            var currentZip by remember { mutableStateOf("55101") } // start with a default

//            for icon codes and for images
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
                        ForecastScreen(viewModel = forecastViewModel,
                            onBackClick = { showForecast = false })
                    }
                } else {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize(),
                        color = Color.Green
                    ) {
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
                                    forecastViewModel.fetchForecast(
                                        currentZip,
                                        BuildConfig.API_KEY
                                    ) // TODO: replace with real zip
                                    showForecast = true
                                },
                                onZipSearch = { zip ->
                                    currentZip = zip
                                    weatherViewModel.fetchWeatherByZip(zip, BuildConfig.API_KEY)
                                    forecastViewModel.fetchForecast(zip, BuildConfig.API_KEY)
                                }

                            )

                        }
                    }
                }
            }

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
    }}