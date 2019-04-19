package com.irfan.bandungweather.models

data class Weather (
    val list: MutableList<WeatherList>,
    val weather: MutableList<WeatherData>,
    val main: MainData
)

data class WeatherList (
    val weather: MutableList<WeatherData>,
    val dt_txt: String
)

data class MainData (
    val temp: String,
    val humidity: String
)

data class WeatherData (
    val main: String,
    val description: String,
    val icon: String
)