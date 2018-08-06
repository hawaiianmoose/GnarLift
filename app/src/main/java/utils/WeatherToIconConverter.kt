package utils

import hawaiianmoose.gnarlift.R

object WeatherToIconConverter {

    fun convertWeatherTextToIcon(weatherText: String): Int {
        val lowercaseWeatherText = weatherText.toLowerCase()

        if (lowercaseWeatherText.contains("cloud") && lowercaseWeatherText.contains("partly")) {
            return R.drawable.ic_weather_partlycloudy
        }
        if (lowercaseWeatherText.contains("sun") || lowercaseWeatherText.contains("clear")) {
            return R.drawable.ic_weather_sunny
        }
        if (lowercaseWeatherText.contains("snow") || lowercaseWeatherText.contains("flur")) {
            return R.drawable.ic_weather_snowy
        }
        if (lowercaseWeatherText.contains("thunder")) {
            return R.drawable.ic_weather_lightning
        }
        if (lowercaseWeatherText.contains("hail")) {
            return R.drawable.ic_weather_hail
        }
        if (lowercaseWeatherText.contains("rain") || lowercaseWeatherText.contains("shower")) {
            return R.drawable.ic_weather_pouring
        }
        if (lowercaseWeatherText.contains("mix")) {
            return R.drawable.ic_weather_snowy_rainy
        }
        if (lowercaseWeatherText.contains("cloud")) {
            return R.drawable.ic_weather_cloudy
        }

        return R.drawable.ic_weather_partlycloudy
    }
}