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

//  ICONS
//        sun
//        cloud
//        snow
//        rain
//        shower
//        thunder

    // CONDITIONS
        //showers
        //thunderstorms
        //clear
        //sunny
        //snow



        //EXAMPLE:
        //"icon": [
        //"icon-cloud",
        //"icon-sunny"
        //],
        //"text": "",
        //"conditions": "Mostly Sunny then Chance Showers And Thunderstorms",

        return R.drawable.ic_weather_snowy //default should be cloud?
    }

}