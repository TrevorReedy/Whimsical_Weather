package com.example.whimsicalweather.ui.screen

import android.content.res.Resources
import android.health.connect.datatypes.units.Pressure
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.whimsicalweather.R
import com.example.whimsicalweather.ui.viewmodel.WeatherViewModel

import kotlin.math.roundToInt



@Composable
fun WeatherScreen(viewModel: WeatherViewModel, innerPadding: PaddingValues = PaddingValues()) {
    // Observe weather data from the ViewModel
    val temperature by viewModel.temperature.observeAsState()
    val cityName by viewModel.cityName.observeAsState()
    val lon by viewModel.lon.observeAsState()
    val lat by viewModel.lat.observeAsState()

    val tempMin by viewModel.tempMin.observeAsState()
    val tempMax by viewModel.tempMax.observeAsState()


    val humidity by viewModel.humidity.observeAsState()
    val pressure by viewModel.pressure.observeAsState()

    val feelsLike by viewModel.feelsLike.observeAsState()




    // Apply padding and display the UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(innerPadding)
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        HeaderRow()
        DisplayWidget(
            temperature = temperature?.roundToInt(),
            cityName,
            feelsLike?.roundToInt(),
            tempMin?.roundToInt() ,
            tempMax?.roundToInt() ,
            humidity,
            pressure,


            )
    }
}

@Composable
fun HeaderRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            modifier = Modifier.padding(10.dp),
            color = Color.Black,
            style = TextStyle(
                fontSize = 25.sp
            )
        )
    }
}

@Composable
fun DisplayWidget(temperature: Int?,
                  cityName: String?,
                  feelsLike: Int?,
                  tempMin: Int?,
                  tempMax: Int?,
                  humidity:Int?,
                  pressure:Int?
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Location(lat =  34.17, lon =  -115.13, cityName)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TempDisplay(
                temperature = stringResource(id = R.string.temp_value, temperature ?: 0),
                feelsLike = stringResource(id =  R.string.feels_like, feelsLike ?: 0)
            )
            Image(
                painter = painterResource(id = R.drawable.sun),
                contentDescription = stringResource(id = R.string.weather_icon_desc),
                modifier = Modifier.size(120.dp)
            )
        }
        WeatherDetails(tempMin, tempMax, humidity, pressure)
    }
}

@Composable
fun TempDisplay(temperature: String, feelsLike: String) {
    Column(
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth(0.33f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = temperature,
            style = TextStyle(
                fontSize = 70.sp
            )
        )
        Text(
            text = "$feelsLike",
            style = TextStyle(
                fontSize = 16.sp
            )
        )
    }
}

@Composable
fun Location(lat: Double, lon: Double,cityName:String?) {
    Row {
        Text(
            text = stringResource(id = R.string.city_name, cityName ?: "----"),
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            style = TextStyle(
                textAlign = TextAlign.Center,
                fontSize = 24.sp
            )
        )
    }
}

@Composable
fun WeatherDetails(low: Int?, high: Int?, humidity: Int?, pressure: Int?) {
    // Handle null values and format the strings
    val lowText = stringResource(
        id = R.string.high_temp,
        low ?: 666 // Default to 0 if null
    )

    val highText = stringResource(
        id = R.string.high_temp,
        high ?: 0 // Default to 0 if null
    )

    val humidityText = stringResource(
        id = R.string.humidity,
        humidity ?: 0 // Default to 0 if null
    )

    val pressureText = stringResource(
        id = R.string.pressure,
        pressure ?: 0 // Default to 0 if null
    )

    // Create a list of details
    val details = listOf(lowText, highText, humidityText, pressureText)

    // Display the details
    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        details.forEach { detail ->
            Text(
                text = detail,
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    fontSize = 18.sp,
                    color = Color.Black
                )
            )
        }
    }
}