package com.example.irfan.myfirstapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.example.irfan.myfirstapp.adapter.WeatherAdapter
import com.example.irfan.myfirstapp.model.Weather
import kotlinx.android.synthetic.main.activity_weather.*

class WeatherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        recyclerWeather.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        AndroidNetworking.initialize(applicationContext)
        AndroidNetworking.get("https://samples.openweathermap.org/data/2.5/forecast?id=7035024&appid=fd04fd5bc71777df45c9cb2b982d5159")
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObject(Weather::class.java, object : ParsedRequestListener<Weather> {
                    override fun onResponse(response: Weather) {
                        recyclerWeather.adapter = WeatherAdapter(applicationContext, response.list)
                    }

                    override fun onError(anError: ANError?) {
                        Toast.makeText(applicationContext, anError?.message, Toast.LENGTH_SHORT).show()
                        print(anError?.message)
                    }

                })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}
