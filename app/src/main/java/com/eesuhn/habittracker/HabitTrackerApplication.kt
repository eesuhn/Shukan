package com.eesuhn.habittracker

import android.app.Application
import com.eesuhn.habittracker.core.common.Telemetry
import dagger.hilt.android.HiltAndroidApp
import logcat.AndroidLogcatLogger
import logcat.LogPriority
import javax.inject.Inject

@HiltAndroidApp(Application::class)
class HabitTrackerApplication : Hilt_HabitTrackerApplication() {

    @Inject
    lateinit var telemetry: Telemetry

    override fun onCreate() {
        super.onCreate()

        telemetry.initialize()

        AndroidLogcatLogger.installOnDebuggableApp(this, minPriority = LogPriority.VERBOSE)
    }
}