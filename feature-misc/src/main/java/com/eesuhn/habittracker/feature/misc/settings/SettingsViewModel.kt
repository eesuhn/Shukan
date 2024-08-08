package com.eesuhn.habittracker.feature.misc.settings

import androidx.lifecycle.ViewModel
import com.eesuhn.habittracker.core.common.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

data class AppInfo(
    val versionName: String,
    val buildType: String,
    val appId: String,
    val urlPrivacyPolicy: String,
    val urlSourceCode: String,
) {
    val marketUrl = "market://details?id=${appId}"
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appPreferences: AppPreferences,
    val appInfo: AppInfo
) : ViewModel() {

    val crashReportingEnabled = MutableStateFlow(true)
    val dynamicColor = appPreferences.dynamicColorEnabled

    init {
        crashReportingEnabled.value = appPreferences.crashReportingEnabled
    }

    fun setCrashReportingEnabled(enabled: Boolean) {
        // Nothing to do other than persisting the preference
        // Bugsnag can't be disabled at runtime, but the next app restart won't initialize it
        appPreferences.crashReportingEnabled = enabled
        crashReportingEnabled.value = enabled
    }

    fun setDynamicColorEnabled(enabled: Boolean) {
        appPreferences.setDynamicColorEnabled(enabled)
    }
}