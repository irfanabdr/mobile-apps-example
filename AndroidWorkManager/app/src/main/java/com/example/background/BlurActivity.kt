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

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_blur.*
import android.content.Intent
import android.text.TextUtils

class BlurActivity : AppCompatActivity() {

    lateinit private var blurViewModel: BlurViewModel

    /**
     * Get the blur level from the radio button as an integer
     * @return Integer representing the amount of times to blur the image
     */
    private val blurLevel: Int
        get() {
            when (radio_blur_group.checkedRadioButtonId) {
                R.id.radio_blur_lv_1 -> return 1
                R.id.radio_blur_lv_2 -> return 2
                R.id.radio_blur_lv_3 -> return 3
            }

            return 1
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blur)

        // Get the ViewModel
        blurViewModel = ViewModelProviders.of(this).get(BlurViewModel::class.java)

        // Image uri should be stored in the ViewModel; put it there then display
        val imageUriExtra = intent.getStringExtra(Constants.KEY_IMAGE_URI)
        blurViewModel.setImageUri(imageUriExtra)
        if (blurViewModel.imageUri != null) {
            Glide.with(this).load(blurViewModel.imageUri).into(image_view)
        }

        // Setup blur image file button
        go_button.setOnClickListener({
            blurViewModel.applyBlur(blurLevel)
        })

        see_file_button.setOnClickListener({
            val currentUri = blurViewModel.outputUri
            if (currentUri != null) {
                val actionView = Intent(Intent.ACTION_VIEW, currentUri)
                if (actionView.resolveActivity(packageManager) != null) {
                    startActivity(actionView)
                }
            }
        })

        cancel_button.setOnClickListener({
            blurViewModel.cancelWork()
        })

        blurViewModel.savedWorkInfo.observe(this, Observer { listOfWorkInfo ->
            if (listOfWorkInfo == null || listOfWorkInfo.isEmpty()) {
                return@Observer
            }

            // We only care about the first output status.
            // Every continuation has only one worker tagged TAG_OUTPUT
            val workInfo = listOfWorkInfo.get(0)

            val finished = workInfo.state.isFinished
            if (!finished) {
                showWorkInProgress()
            } else {
                showWorkFinished()
                val outputData = workInfo.outputData
                val outputImageUri = outputData.getString(Constants.KEY_IMAGE_URI)

                // If there is an output file show "See File" button
                if (!TextUtils.isEmpty(outputImageUri)) {
                    if (outputImageUri != null) {
                        blurViewModel.setOutputUri(outputImageUri)
                        see_file_button.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    /**
     * Shows and hides views for when the Activity is processing an image
     */
    private fun showWorkInProgress() {
        progress_bar.visibility = View.VISIBLE
        cancel_button.visibility = View.VISIBLE
        go_button.visibility = View.GONE
        see_file_button.visibility = View.GONE
    }

    /**
     * Shows and hides views for when the Activity is done processing an image
     */
    private fun showWorkFinished() {
        progress_bar.visibility = View.GONE
        cancel_button.visibility = View.GONE
        go_button.visibility = View.VISIBLE
    }
}