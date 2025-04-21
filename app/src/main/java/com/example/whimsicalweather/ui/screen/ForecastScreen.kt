package com.example.whimsicalweather.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.whimsicalweather.R
import com.example.whimsicalweather.data.model.DailyForecast
import com.example.whimsicalweather.ui.viewmodel.ForecastViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ForecastScreen(viewModel: ForecastViewModel, onBackClick: () -> Unit) {
    val forecastData = viewModel.forecast.observeAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        forecastData.value?.let { forecast ->
            Column {Button(
                onClick = { onBackClick() },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text("Back")
            }
                Text(
                    text = "7-Day Forecast: ${forecast.city.name}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(forecast.list) { day ->
                        ForecastItem(day)
                    }
                }
            }
        } ?: Text("Loading...", color = Color.White)
    }
}

@Composable
fun ForecastItem(day: DailyForecast) {
    val date = SimpleDateFormat("EEE, MMM d", Locale.getDefault()).format(Date(day.dt * 1000))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray.copy(alpha = 0.5f))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = date, fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Medium)
            Text(text = "${day.weather.firstOrNull()?.description ?: "No description"}", color = Color.White)
            Text(text = "High: ${day.temp.max}°  Low: ${day.temp.min}°", color = Color.White)
        }

        // Optional: Display weather icon based on `day.weather.first().icon`
        Image(
            painter = painterResource(id = R.drawable.sun), // Replace this with dynamic icons later
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            contentScale = ContentScale.Fit
        )
    }
}
