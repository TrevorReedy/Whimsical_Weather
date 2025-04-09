package com.example.whimsicalweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.whimsicalweather.R
import com.example.whimsicalweather.ui.theme.WhimsicalWeatherTheme





class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WhimsicalWeatherTheme {  // Remove the empty curly braces here
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DefaultScreen(innerPadding = innerPadding)
                }
            }
        }
    }
}

@Composable
fun DefaultScreen(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(innerPadding) // Apply innerPadding here
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        HeaderRow()
        DisplayWidget()

    }
}

@Composable
fun HeaderRow(
){
    Row(
        modifier = Modifier.fillMaxWidth()
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
fun DisplayWidget() {
    Column(
        modifier = Modifier
            .fillMaxWidth() // Fills the entire width of the screen
            .background(Color.White) // Optional: Set a background color
    ) {
        CityName()
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TempDisplay(
            )
            Image(
                painter = painterResource(id = R.drawable.sun),
                contentDescription = stringResource(id = R.string.weather_icon_desc),
                modifier = Modifier.size(120.dp)
            )
        }
        Row(){
            WeatherDetails()

        }
    }
}

@Composable
fun TempDisplay(){
    Column(
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth(0.33f),
        verticalArrangement = Arrangement.Center, // Centers vertically
        horizontalAlignment = Alignment.CenterHorizontally // Centers horizontally


    ) {
        Text(
            text = stringResource(id = R.string.temp_value),
            modifier = Modifier,
            style = TextStyle(
                fontSize = 70.sp

            )
        )
        Text(
            text = stringResource(id = R.string.feels_like),
            modifier = Modifier,
            style = TextStyle(
                fontSize = 16.sp

            )
        )


    }

}
@Composable
fun CityName(){
    Row(

    ) { Text(
        text = stringResource(id = R.string.city_name),
        modifier = Modifier.fillMaxWidth().padding(15.dp),
        style = TextStyle(
            textAlign = TextAlign.Center, // Aligns text horizontally in the center
            fontSize = 24.sp,




            )
    ) }
}

@Composable
fun WeatherDetails() {
    val weatherDetails = listOf(
        stringResource(id = R.string.low_temp),
        stringResource(id = R.string.high_temp),
        stringResource(id = R.string.humidity),
        stringResource(id = R.string.pressure)
    )

    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally // Centers horizontally
    ) {
        weatherDetails.forEach { detail ->
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

