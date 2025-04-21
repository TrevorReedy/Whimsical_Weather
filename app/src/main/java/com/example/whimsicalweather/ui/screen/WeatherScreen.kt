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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.whimsicalweather.ui.viewmodel.WeatherViewModel
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

import kotlin.math.roundToInt


//clock functionallity

import java.util.Calendar



@Composable
fun WeatherScreen(viewModel: WeatherViewModel,
                  innerPadding: PaddingValues = PaddingValues(),
                  onForecastClick: () -> Unit) {
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
            Spacer(modifier = Modifier.height(12.dp))
            Clock()

            DisplayWidget(
                temperature = temperature?.roundToInt(),
                cityName,
                feelsLike?.roundToInt(),
                tempMin?.roundToInt(),
                tempMax?.roundToInt(),
                humidity,
                pressure,
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
                fontSize = 80.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        )
        Text(
            text = stringResource(id = R.string.date, dayOfWeek, month, day),
            style = TextStyle(
                fontSize = 25.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        )
    }
}



//@Composable
//fun Clock(
//){
//    val calendar = Calendar.getInstance()
//
//    val hour = calendar.get(Calendar.HOUR_OF_DAY)
//    val minute = calendar.get(Calendar.MINUTE)
//    val second = calendar.get(Calendar.SECOND)
//
//
//    val unformatedMonth = calendar.get(Calendar.MONTH)
//    val monthsList = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
//    val month = monthsList[unformatedMonth]
//
//
//    val unformatedDay = calendar.get(Calendar.DAY_OF_WEEK)
//    val dayOfWeekList = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
//    val dayOfWeek = dayOfWeekList[unformatedDay - 1]
//
//    val day = calendar.get(Calendar.DAY_OF_MONTH)
//    Column (
//
//    ) {
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth(),
//
//            horizontalArrangement = Arrangement.Center
//        ) {
//            Text(
//                text = stringResource(id = R.string.time, hour, minute),
//                modifier = Modifier,
//                style = TextStyle(
//                    fontSize = 80.sp,
//                    color = Color.White,
//                    textAlign = TextAlign.Center
//
//                )
//            )
//        }
//        }
//
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.Center // Center the child horizontally
//        ) {
//
//                Text(
//                    text = stringResource(id = R.string.date, dayOfWeek, month, day),
//                    style = TextStyle(
//                        fontSize = 25.sp,
//                        color = Color.White,
//                        textAlign = TextAlign.Center
//
//                    )
//                )
//            }
//        }


@Composable
fun DisplayWidget(temperature: Int?,
                  cityName: String?,
                  feelsLike: Int?,
                  tempMin: Int?,
                  tempMax: Int?,
                  humidity:Int?,
                  pressure:Int?,
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
        WeatherDetails(temperature, tempMin, tempMax, humidity, feelsLike, cityName,onForecastClick)
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

//@Composable
//fun Location(lat: Double, lon: Double,cityName:String?) {
//    Row {
//        Text(
//            text = stringResource(id = R.string.city_name, cityName ?: "----"),
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(15.dp),
//            style = TextStyle(
//                textAlign = TextAlign.Center,
//                color = Color.White,
//                fontSize = 24.sp
//            )
//        )
//    }
//}
@Composable
fun WeatherDetails(
    temperature: Int?,
    low: Int?,
    high: Int?,
    humidity: Int?,
    feelsLike: Int?,
    CityName: String?,
    onForecastClick: () -> Unit
) {
    val lowText = stringResource(id = R.string.low_temp, low ?: 0)
    val highText = stringResource(id = R.string.high_temp, high ?: 0)
    val humidityText = stringResource(id = R.string.humidity, humidity ?: 0)
    val temperatureText = stringResource(id = R.string.temp_value, temperature ?: 0)
    val feelsLikeText = stringResource(id = R.string.feels_like, feelsLike ?: 0)
    val cityName = stringResource(id = R.string.city_name, CityName ?: "----")

    val details = listOf(feelsLikeText, highText, lowText, humidityText)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                color = Color.Gray.copy(alpha = 0.6f),
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
                    painter = painterResource(id = R.drawable.sun),
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
                ) {
                    Text(text = "View Forecast")
                }
            }
        }
    }
}


//@Composable
//fun WeatherDetails(temperature: Int?,low: Int?, high: Int?, humidity: Int?, feelsLike: Int?) {
////    // Handle null values and format the strings
//    val lowText = stringResource(
//        id = R.string.low_temp,
//        low ?: 0 // Default to 0 if null
//
//    )
//    val highText = stringResource(
//        id = R.string.high_temp,
//        high ?: 0 // Default to 0 if null
//    )
////
//    val humidityText = stringResource(
//        id = R.string.humidity,
//        humidity ?: 0 // Default to 0 if null
//    )
//////
//    val temperatureText = stringResource(
//        id = R.string.temp_value,
//        temperature ?: 0 // Default to 0 if null
//    )
//    val feelsLikeText = stringResource(
//        id =  R.string.feels_like,
//        feelsLike ?: 0)
////
////    // Create a list of details
////    val details = listOf(lowText, highText, humidityText, pressureText)
//
//
//    val details = listOf(feelsLikeText, highText,lowText, humidityText)
//    // Display the details
//    Row(
//        modifier = Modifier.fillMaxWidth()
//            .fillMaxHeight()
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//                .background(
//                    color = Color.Gray.copy(alpha = 0.6f), // 60% opacity
//                    shape = RoundedCornerShape(12.dp)      // Optional rounded corners
//                )
//                .padding(16.dp) // inner padding
//        ) {
//            Column(
//                modifier = Modifier
//                    .weight(1f)
//                    .fillMaxHeight()
//                    .padding(20.dp),
////            horizontalAlignment = Alignment.CenterHorizontally,
//
//                verticalArrangement = Arrangement.Center
//            ) {
//
//                Text(
//                    text = temperatureText,
//                    modifier = Modifier.fillMaxWidth(),
//                    style = TextStyle(
//                        fontSize = 50.sp,
//                        color = Color.White,
//                        fontWeight = FontWeight.Bold
//                    )
//
//                )
//
//
//
//                details.forEach { detail ->
//                    Text(
//                        text = detail,
//                        modifier = Modifier.fillMaxWidth(),
//                        style = TextStyle(
//                            fontSize = 18.sp,
//                            color = Color.White,
//
//                            )
//                    )
//                }
//            }
//            Column(
//                modifier = Modifier
//                    .weight(1f),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Bottom
//
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.sun),
//                    contentDescription = stringResource(id = R.string.weather_icon_desc),
//                    modifier = Modifier.fillMaxSize(),
//
//                    )
//            }
//        }
//
//    }
//}