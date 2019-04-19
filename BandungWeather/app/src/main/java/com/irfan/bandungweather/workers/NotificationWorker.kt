package com.irfan.bandungweather.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.support.v4.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.irfan.bandungweather.R
import com.irfan.bandungweather.models.Weather

class NotificationWorker(appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {

    override fun doWork(): Result {
        getCurrentWeather()
        return Result.success()
    }

    private fun getCurrentWeather() {
        AndroidNetworking.initialize(applicationContext)
        AndroidNetworking.get("https://api.openweathermap.org/data/2.5/weather?q=Bandung&units=metric&appid=28092cde69ef8590d54e9b0877d516fc")
            .setTag(this)
            .setPriority(Priority.LOW)
            .build()
            .getAsObject(Weather::class.java, object : ParsedRequestListener<Weather> {
                override fun onResponse(response: Weather) {
                    val weather = response.weather[0]
                    val title = weather.main
                    val desc = "Current weather is " + weather.description +
                            " with temperature " + response.main.temp + " \u2103"

                    displayNotification(title, desc)
                }

                override fun onError(anError: ANError?) {
                    print(anError?.message)
                }

            })
    }


    private fun displayNotification(title: String, desc: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel("weather_notification", "weather_notification", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, "weather_notification")
            .setContentTitle(title)
            .setContentText(desc)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(desc))
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setPriority(NotificationCompat.PRIORITY_LOW)

        notificationManager.notify(1, notification.build())
    }
}