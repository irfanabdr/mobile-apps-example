package com.irfan.bandungweather.fragments

import android.os.Bundle
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.widget.Toast
import androidx.work.*
import com.irfan.bandungweather.workers.NotificationWorker
import com.irfan.bandungweather.R
import java.util.concurrent.TimeUnit

class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
        findPreference(getString(R.string.key_notification)).setOnPreferenceChangeListener(this)
    }
    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
        val enabled = newValue as Boolean
        if (enabled) {
            enableNotification()
        } else {
            disableNotification()
        }

        return true
    }

    private fun enableNotification() {
        val constraints = Constraints.Builder().setRequiresBatteryNotLow(true).build()
        val periodicWorkRequest = PeriodicWorkRequest.Builder(NotificationWorker::class.java,
            6, TimeUnit.HOURS, 6, TimeUnit.HOURS)
            .setConstraints(constraints)
            .addTag(NotificationWorker::class.java.simpleName)
            .build()
        WorkManager.getInstance().enqueueUniquePeriodicWork(NotificationWorker::class.java.simpleName,
            ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest)
        Toast.makeText(context, getString(R.string.notification_enabled), Toast.LENGTH_SHORT).show()
    }

    private fun disableNotification() {
        WorkManager.getInstance().cancelAllWorkByTag(NotificationWorker::class.java.simpleName)
        Toast.makeText(context, getString(R.string.notification_disabled), Toast.LENGTH_SHORT).show()
    }
}
