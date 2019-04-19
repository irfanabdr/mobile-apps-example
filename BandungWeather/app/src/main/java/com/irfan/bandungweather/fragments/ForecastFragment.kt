package com.irfan.bandungweather.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.irfan.bandungweather.R
import com.irfan.bandungweather.adapters.WeatherAdapter
import com.irfan.bandungweather.models.Weather
import kotlinx.android.synthetic.main.fragment_forecast.*

class ForecastFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forecast, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_forecast.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        recycler_forecast.visibility = GONE
        progress_bar_forecast.visibility = VISIBLE
        getWeatherForecast()
    }

    private fun getWeatherForecast() {
        AndroidNetworking.initialize(context)
        AndroidNetworking.get("http://api.openweathermap.org/data/2.5/forecast?q=Bandung&units=metric&appid=28092cde69ef8590d54e9b0877d516fc")
            .setTag(this)
            .setPriority(Priority.LOW)
            .build()
            .getAsObject(Weather::class.java, object : ParsedRequestListener<Weather> {
                override fun onResponse(response: Weather) {
                    recycler_forecast.adapter = WeatherAdapter(response.list)
                    progress_bar_forecast.visibility = GONE
                    recycler_forecast.visibility = VISIBLE
                }

                override fun onError(anError: ANError?) {
                    progress_bar_forecast.visibility = GONE
                    Toast.makeText(context, anError?.message, Toast.LENGTH_SHORT).show()
                    print(anError?.message)
                }

            })
    }

}
