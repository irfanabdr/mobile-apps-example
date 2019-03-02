package com.example.irfan.myfirstapp.model


data class Weather ( val list: MutableList<WeatherList> )

data class WeatherList (
        val weather: MutableList<WeatherData>,
        val dt_txt: String
)

data class WeatherData (
        val main: String,
        val description: String,
        val icon: String
)