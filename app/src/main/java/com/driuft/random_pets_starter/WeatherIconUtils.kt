package com.driuft.random_pets_starter

object WeatherIconUtils {
    fun getDrawableResourceForWeatherIcon(iconCode: String): Int? {
        return when (iconCode) {
            "01d" -> R.drawable.day_clear
            "01n" -> R.drawable.night_full_moon_clear
            "02d" -> R.drawable.day_partial_cloud
            "02n" -> R.drawable.night_full_moon_partial_cloud
            "03d" -> R.drawable.cloudy
            "03n" ->R.drawable.night_half_moon_partial_cloud
            "04d" -> R.drawable.angry_clouds
            "04n" -> R.drawable.night_full_moon_sleet
            "09d" -> R.drawable.rain
            "09n" -> R.drawable.rain
            "10d" -> R.drawable.day_rain
            "10n" ->R.drawable.night_half_moon_rain
            "11d" -> R.drawable.day_rain_thunder
            "11n" -> R.drawable.night_full_moon_rain_thunder
            "13d" -> R.drawable.snow
            "13n" ->R.drawable.snow


            // ... map other icon codes ...
            else -> null // A default icon
        }
    }
}