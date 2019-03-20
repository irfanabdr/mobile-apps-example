package com.irfan.workrequest

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.work.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Periodic Work Request
        val constraints = Constraints.Builder().setRequiresBatteryNotLow(true).build()

        val periodicWorkRequest = PeriodicWorkRequest.Builder(MyWorker::class.java,
            14, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance().enqueueUniquePeriodicWork(MyWorker.TASK_DESC,
            ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest)

        buttonEnqueue.setOnClickListener {
            if (!editTextSchedule.text.isEmpty()) {
                initializeWorkRequest(editTextSchedule.text.toString().toLong())
                return@setOnClickListener
            }

            Toast.makeText(applicationContext, getString(R.string.empty_schedule), Toast.LENGTH_SHORT).show()
        }
    }

    /*
    * Method for creating one time work request
    * */
    private fun initializeWorkRequest(minutes: Long) {
        if (textViewStatus.visibility == View.GONE) textViewStatus.visibility = View.VISIBLE
        textViewStatus.text = ""
        editTextSchedule.isEnabled = false
        buttonEnqueue.isEnabled = false
        buttonEnqueue.text = getString(R.string.work_in_queue)

        val data = Data.Builder()
            .putString(MyWorker.TASK_DESC, "The task data passed from MainActivity")
            .build()

        // One Time Work Request
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .setInitialDelay(minutes, TimeUnit.MINUTES)
            .setInputData(data)
            .build()
        WorkManager.getInstance().enqueue(oneTimeWorkRequest)

        WorkManager.getInstance().getWorkInfoByIdLiveData(oneTimeWorkRequest.id).observe(
            this, Observer {
                if (!textViewStatus.text.isEmpty())
                    textViewStatus.append("\n")

                if (it != null && it.state.isFinished) {
                    textViewStatus.append(it.outputData.getString(MyWorker.TASK_DESC))
                    editTextSchedule.isEnabled = true
                    buttonEnqueue.isEnabled = true
                    buttonEnqueue.text = getString(R.string.enqueue_work)
                } else {
                    textViewStatus.append(it?.state?.name)
                }
            }
        )

    }
}
