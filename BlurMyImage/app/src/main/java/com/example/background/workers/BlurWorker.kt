package com.example.background.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.util.Log
import com.example.background.Constants
import android.net.Uri
import androidx.work.Data

class BlurWorker(appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {

    private val TAG = BlurWorker::class.java.simpleName

    override fun doWork(): Result {
        val resourceUri = inputData.getString(Constants.KEY_IMAGE_URI)

        try {
            if (TextUtils.isEmpty(resourceUri)) {
                Log.e(TAG, "Invalid input uri")
                throw IllegalArgumentException("Invalid input uri")
            }

            val resolver = applicationContext.contentResolver
            // Create a bitmap
            val picture = BitmapFactory.decodeStream(resolver.openInputStream(Uri.parse(resourceUri)))
            val output = WorkerUtils.blurBitmap(picture, applicationContext)
            val outputUri = WorkerUtils.writeBitmapToFile(applicationContext, output)
            WorkerUtils.makeStatusNotification("Output is $outputUri", applicationContext)
            val outputData = Data.Builder()
                    .putString(Constants.KEY_IMAGE_URI, outputUri.toString())
                    .build()

            return Result.success(outputData)
        } catch (throwable: Throwable) {
            Log.e(TAG, "Error applying blur", throwable)
            return Result.failure()
        }
    }

}