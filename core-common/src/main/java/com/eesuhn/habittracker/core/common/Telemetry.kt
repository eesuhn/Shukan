package com.eesuhn.habittracker.core.common

import android.content.Context
import android.os.Build
import android.os.StrictMode
import com.bugsnag.android.BreadcrumbType
import com.bugsnag.android.Bugsnag
import com.bugsnag.android.BugsnagThreadViolationListener
import com.bugsnag.android.BugsnagVmViolationListener
import logcat.LogPriority
import logcat.asLog
import logcat.logcat

interface Telemetry {

    enum class BreadcrumbType {
        Navigation,
        State,
        UserAction,
    }

    fun initialize()

    fun logNonFatal(e: Throwable)

    fun leaveBreadcrumb(message: String, metadata: Map<String, Any>, type: BreadcrumbType)

}

class TelemetryImpl(
    private val appContext: Context,
    private val appPreferences: AppPreferences
) : Telemetry {

    override fun initialize() {
        if (!appPreferences.crashReportingEnabled) return

        Bugsnag.start(appContext)
        initStrictModeListener()
    }

    override fun logNonFatal(e: Throwable) {
        logcat(LogPriority.ERROR) { e.asLog() }
        if (appPreferences.crashReportingEnabled) {
            Bugsnag.notify(e)
        }
    }

    override fun leaveBreadcrumb(
        message: String,
        metadata: Map<String, Any>,
        type: Telemetry.BreadcrumbType
    ) {
        if (!appPreferences.crashReportingEnabled) return

        val bugsnagType = when (type) {
            Telemetry.BreadcrumbType.Navigation -> BreadcrumbType.NAVIGATION
            Telemetry.BreadcrumbType.State -> BreadcrumbType.STATE
            Telemetry.BreadcrumbType.UserAction -> BreadcrumbType.USER
        }
        Bugsnag.leaveBreadcrumb(message, metadata, bugsnagType)
    }

    private fun initStrictModeListener() {
        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val executor = appContext.mainExecutor

            // setup StrictMode policy for thread violations
            val threadListener = BugsnagThreadViolationListener()
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .permitDiskReads()
                    .permitDiskWrites()
                    .penaltyListener(executor, threadListener)
                    .penaltyLog()
                    .build()
            )

            // setup StrictMode policy for VM violations
            val vmListener = BugsnagVmViolationListener()
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyListener(executor, vmListener)
                    .penaltyLog()
                    .build()
            )
        }
    }

}