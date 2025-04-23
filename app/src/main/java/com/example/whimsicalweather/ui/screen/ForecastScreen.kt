package com.example.whimsicalweather.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
    val iconCode = viewModel.icon.observeAsState()
    val cityName by viewModel.cityName.observeAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        forecastData.value?.let { forecast ->
            Column {Button(
                onClick = { onBackClick() },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Spacer(modifier = Modifier.width(8.dp)
                    .height(16.dp))
                Text("Back")
            }
                Text(
                    text = "7-Day Forecast: ${cityName}",
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
    val iconCode = day.weather.firstOrNull()?.icon ?: "01d"
    val iconRes = getIcon(iconCode)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray.copy(alpha = 0.7f))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = date,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = day.weather.firstOrNull()?.description?.replaceFirstChar { it.uppercase() } ?: "No description",
                fontSize = 18.sp,
                color = Color.LightGray.copy(alpha = 0.5f),

            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "High: ${day.temp.max}°  Low: ${day.temp.min}°",
                fontSize = 16.sp,
                color = Color.White
            )
        }

        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .padding(start = 12.dp),
            contentScale = ContentScale.Fit
        )
    }
}





