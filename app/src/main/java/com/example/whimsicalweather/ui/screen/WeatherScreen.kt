package com.example.whimsicalweather.ui.screen

import android.content.res.Resources
import android.health.connect.datatypes.units.Pressure
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.whimsicalweather.R
import com.example.whimsicalweather.ui.viewmodel.WeatherViewModel import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType

import kotlin.math.roundToInt


//clock functionallity

import java.util.Calendar



@Composable
fun WeatherScreen(viewModel: WeatherViewModel,
                  innerPadding: PaddingValues = PaddingValues(),
                  onForecastClick: () -> Unit,
                  onZipSearch: (String) -> Unit) {
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

    val iconCode by viewModel.icon.observeAsState("01d")

    var zipCode by remember { mutableStateOf("") }



    // Apply padding and display the UI
    Box(
        modifier = Modifier
            .fillMaxSize(),

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    WindowInsets.statusBars.asPaddingValues() // handles notch, camera, etc
                )
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(2.dp))
            Clock()
            ZipCodeField(
                zipCode = zipCode,
                onZipChange = { zipCode = it },
                onSubmit = {
                    if (zipCode.length == 5) {
                        onZipSearch(zipCode)
                    }
                }
            )

            DisplayWidget(
                temperature = temperature?.roundToInt(),
                cityName,
                feelsLike?.roundToInt(),
                tempMin?.roundToInt(),
                tempMax?.roundToInt(),
                humidity,
                pressure,
                iconCode ?: "01d",
                onForecastClick

                )
        }
    }
}




@Composable
fun Clock() {
    val calendar = Calendar.getInstance()

    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val unformattedMonth = calendar.get(Calendar.MONTH)
    val month = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )[unformattedMonth]

    val dayOfWeek = listOf(
        "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
    )[calendar.get(Calendar.DAY_OF_WEEK) - 1]

    val day = calendar.get(Calendar.DAY_OF_MONTH)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.time, hour, minute),
            style = TextStyle(
                fontSize = 90.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            text = stringResource(id = R.string.date, dayOfWeek, month, day),
            style = TextStyle(
                fontSize = 32.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold
                )
        )

    }
}



@Composable
fun ZipCodeField(
    zipCode: String,
    onZipChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = zipCode,
            onValueChange = {
                if (it.length <= 5 && it.all { char -> char.isDigit() }) {
                    onZipChange(it)
                }
            },
            placeholder = { Text("Enter Zip Code") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { onSubmit() },
            enabled = zipCode.length == 5,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2D9CDB),
                contentColor = Color.White
            )
        ) {
            Text("Search")
        }
    }
}



@Composable
fun DisplayWidget(temperature: Int?,
                  cityName: String?,
                  feelsLike: Int?,
                  tempMin: Int?,
                  tempMax: Int?,
                  humidity:Int?,
                  pressure:Int?,
                  iconCode: String,
                  onForecastClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
//        Location(lat =  34.17, lon =  -115.13, )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        )
        {}
        WeatherDetails(temperature, tempMin, tempMax, humidity, feelsLike, cityName, iconCode , onForecastClick)
    }
}

@Composable
fun TempDisplay(temperature: String) {
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
    }
}

@Composable
fun getWeatherBackground(iconCode: String): Int {
    return when (iconCode) {
        "01d" -> R.drawable.lofi_day
        "01n" -> R.drawable.lofi_night
        "02d", "03d",  "04d" -> R.drawable.lofi_cloudy_day
        "02n", "03n", "04n" -> R.drawable.lofi_cloudy_night

        "09d", "10d"  -> R.drawable.lofi_rainy_day
        "09n", "10n" -> R.drawable.lofi_rain_night
        "11d", "11n" -> R.drawable.lofi_thunderstorm
        "13d", "13n" -> R.drawable.lofi_snow
        "50d" -> R.drawable.lofi_mist_day
        "50n" -> R.drawable.lofi_mist_night
        else -> R.drawable.lofi_day
    }
}

@Composable
fun getIcon(iconCode: String): Int {
    return when (iconCode) {
        "01d" -> R.drawable.sun
        "01n" -> R.drawable.moon
        "02d", "03d",  "04d" -> R.drawable.cloudy_day
        "02n", "03n", "04n" -> R.drawable.cloudy_night

        "09d", "10d"  -> R.drawable.rain_day
        "09n", "10n" -> R.drawable.rainy_night
        "11d", "11n" -> R.drawable.thunderstorm
        "13d", "13n" -> R.drawable.snow
        "50d",  "50n"  -> R.drawable.mist

        else -> R.drawable.sun
    }
}
@Composable
fun WeatherDetails(
    temperature: Int?,
    low: Int?,
    high: Int?,
    humidity: Int?,
    feelsLike: Int?,
    CityName: String?,
    iconCode: String,
    onForecastClick: () -> Unit
) {
    val lowText = stringResource(id = R.string.low_temp, low ?: 0)
    val highText = stringResource(id = R.string.high_temp, high ?: 0)
    val humidityText = stringResource(id = R.string.humidity, humidity ?: 0)
    val temperatureText = stringResource(id = R.string.temp_value, temperature ?: 0)
    val feelsLikeText = stringResource(id = R.string.feels_like, feelsLike ?: 0)
    val cityName =  (CityName ?: "----")

    val details = listOf(feelsLikeText, highText, lowText, humidityText)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
//                color = Color.Gray.copy(alpha = 0.6f),
                color = Color(0x001E1E1E),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = cityName,
                    style = TextStyle(
                        fontSize = 24.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                )
                Text(
                    text = temperatureText,
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(
                        fontSize = 36.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                details.forEach { detail ->
                    Text(
                        text = detail,
                        modifier = Modifier.fillMaxWidth(),
                        style = TextStyle(
                            fontSize = 18.sp,
                            color = Color.White,
                            textAlign = TextAlign.Start
                        )
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Image(
                    painter = painterResource(id = getIcon(iconCode)),
                    contentDescription = stringResource(id = R.string.weather_icon_desc),
                    modifier = Modifier
                        .size(100.dp)
                        .padding(end = 16.dp),
                    contentScale = ContentScale.Fit
                )
                Button(
                    onClick = { onForecastClick() },
                    modifier = Modifier
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.DarkGray.copy(alpha = 0.7f),       // Background color
                        contentColor = Color.White
                )) {
                    Text(text = "View Forecast")
                }
            }
        }
    }
}


