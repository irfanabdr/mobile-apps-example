/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.background

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.net.Uri
import android.text.TextUtils
import androidx.work.*
import com.example.background.workers.BlurWorker
import com.example.background.workers.SaveImageToFileWorker
import com.example.background.workers.CleanupWorker

class BlurViewModel : ViewModel() {

    var imageUri: Uri? = null
    var outputUri: Uri? = null
    private var workManager: WorkManager = WorkManager.getInstance()
    var savedWorkInfo: LiveData<MutableList<WorkInfo>> = workManager.getWorkInfosByTagLiveData(Constants.TAG_OUTPUT)

    fun applyBlur(blurLevel: Int) {
        // Add WorkRequest to Cleanup temporary images
        var continuation = workManager.beginUniqueWork(Constants.IMAGE_MANIPULATION_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequest.from(CleanupWorker::class.java))

        // Add WorkRequest to blur the image
        for (i in 0 until blurLevel) {
            val blurBuilder = OneTimeWorkRequest.Builder(BlurWorker::class.java)

            // Input the Uri if this is the first blur operation
            // After the first blur operation the input will be the output of previous
            // blur operations.
            if (i == 0) {
                blurBuilder.setInputData(createInputDataForUri())
            }

            continuation = continuation.then(blurBuilder.build())
        }

        // Create charging and storage constraints
        val constraints = Constraints.Builder()
                .setRequiresCharging(true)
                .setRequiresStorageNotLow(true)
                .build()

        // Add WorkRequest to save the image to the filesystem
        val save = OneTimeWorkRequest.Builder(SaveImageToFileWorker::class.java)
                .setConstraints(constraints) // This adds the Constraints
                .addTag(Constants.TAG_OUTPUT)
                .build()
        continuation = continuation.then(save)

        // Actually start the work
        continuation.enqueue()
    }

    // Check and convert string to uri
    private fun uriOrNull(uriString: String): Uri? {
        return if (!TextUtils.isEmpty(uriString)) {
            Uri.parse(uriString)
        } else null
    }

    /**
     * Creates the input data bundle which includes the Uri to operate on
     * @return Data which contains the Image Uri as a String
     */
    private fun createInputDataForUri(): Data {
        val builder: Data.Builder = Data.Builder()
        builder.putString(Constants.KEY_IMAGE_URI, imageUri.toString())

        return builder.build()
    }

    // Set image uri from string
    fun setImageUri(uri: String) {
        imageUri = uriOrNull(uri)
    }

    // Set output uri from string
    fun setOutputUri(outputImageUri: String) {
        outputUri = uriOrNull(outputImageUri)
    }

    /**
     * Cancel work using the work's unique name
     */
    fun cancelWork() {
        workManager.cancelUniqueWork(Constants.IMAGE_MANIPULATION_WORK_NAME)
    }

}