package com.example.background.workers

import android.content.Context
import android.text.TextUtils
import androidx.work.Worker
import androidx.work.WorkerParameters
import android.util.Log
import com.example.background.Constants
import java.io.File
import java.lang.Exception

class CleanupWorker(appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {

    private val TAG = CleanupWorker::class.java.simpleName

    override fun doWork(): Result {
        try {
            val outputDirectory = File(applicationContext.filesDir, Constants.OUTPUT_PATH)
            if (outputDirectory.exists()) {
                val entries = outputDirectory.listFiles()
                if (entries != null && entries.size > 0) {
                    for (entry: File in entries) {
                        val name = entry.name
                        if (!TextUtils.isEmpty(name) && name.endsWith(".png")) {
                            val deleted = entry.delete()
                            Log.i(TAG, String.format("Deleted %s - %s", name, deleted));
                        }
                    }
                }
            }

            return Result.success()
        } catch (exception: Exception) {
            Log.e(TAG, "Error cleaning up", exception)
            return Result.failure()
        }
    }

}