package utils

import hawaiianmoose.gnarlift.R

object WeatherToIconConverter {

    fun convertWeatherTextToIcon(weatherText: String): Int {
//        if (weatherText.contains("sun")) {
//            return R.drawable.sunny
//        }
//        if (weatherText.contains("snow")) {
//            return R.drawable.ic_weather_snowy
//        }


//        sun
//        cloud
//        snow
//        rain
//        shower
//        thunder

        return R.drawable.ic_weather_snowy //default should be cloud?
    }

}