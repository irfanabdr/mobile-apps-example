package com.irfan.workrequest

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import android.support.v4.app.NotificationCompat
import android.app.NotificationManager
import android.app.NotificationChannel
import androidx.work.Data

class MyWorker(appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {

    companion object {
        const val TASK_DESC = "task_desc"
    }

    override fun doWork(): Result {
        val taskDesc = inputData.getString(TASK_DESC)
        taskDesc?.let { displayNotification("MyWorkRequest", it) }
        val data = Data.Builder()
            .putString(TASK_DESC, "The conclusion of the task")
            .build()

        return Result.success(data)
    }

    /*
    * Method for displaying notification
    * */
    private fun displayNotification(title: String, task: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel("my_notification", "my_notification", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, "my_notification")
            .setContentTitle(title)
            .setContentText(task)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(LongArray(0))

        notificationManager.notify(1, notification.build())
    }
}