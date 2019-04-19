package com.irfan.bandungweather.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.irfan.bandungweather.R
import com.irfan.bandungweather.models.Weather
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_current.*

class CurrentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_current, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layout_main.visibility = GONE
        progress_bar_current.visibility = VISIBLE
        getCurrentWeather()
    }

    private fun getCurrentWeather() {
        AndroidNetworking.initialize(context)
        AndroidNetworking.get("https://api.openweathermap.org/data/2.5/weather?q=Bandung&units=metric&appid=28092cde69ef8590d54e9b0877d516fc")
            .setTag(this)
            .setPriority(Priority.LOW)
            .build()
            .getAsObject(Weather::class.java, object : ParsedRequestListener<Weather> {
                override fun onResponse(response: Weather) {
                    val weather = response.weather[0]
                    Picasso.get()
                        .load("http://openweathermap.org/img/w/" + weather.icon + ".png")
                        .into(img_weather)
                    txt_weather.text = weather.main
                    txt_description.text = weather.description
                    txt_temperature.text = response.main.temp + " \u2103"
                    txt_humidity.text = response.main.humidity + " %"

                    progress_bar_current.visibility = GONE
                    layout_main.visibility = VISIBLE
                }

                override fun onError(anError: ANError?) {
                    progress_bar_current.visibility = GONE

                    Toast.makeText(context, anError?.message, Toast.LENGTH_SHORT).show()
                    print(anError?.message)
                }

            })
    }

}
