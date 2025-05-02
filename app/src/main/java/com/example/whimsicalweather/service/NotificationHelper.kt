package com.example.whimsicalweather.service

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.whimsicalweather.MainActivity
import com.example.whimsicalweather.R
import com.example.whimsicalweather.data.model.WeatherResponse

object NotificationHelper {
    fun createWeatherNotification(
        context: Context,
        weather: WeatherResponse
    ): Notification {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, "weather_channel")
            .setContentTitle(weather.name)
            .setContentText("${weather.main.temp.toInt()}°F - ${weather.weather[0].main}")
            .setSmallIcon(getNotificationIcon(weather.weather[0].icon))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(true)
            .build()
    }

    private fun getNotificationIcon(iconCode: String): Int {
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
}