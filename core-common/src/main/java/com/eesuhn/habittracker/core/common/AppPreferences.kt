package com.eesuhn.habittracker.core.common

import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

private const val KEY_DASHBOARD_CONFIG = "dashboard_config"
private const val KEY_ONBOARDING_FIRST_HABIT_CREATED = "onboarding_first_habit_created"
private const val KEY_ONBOARDING_FIRST_ACTION_COMPLETED = "onboarding_first_action_completed"
private const val KEY_ONBOARDING_HABIT_DETAILS_OPENED = "onboarding_habit_details_opened"
private const val KEY_ONBOARDING_INSIGHTS_OPENED = "onboarding_insights_opened"
private const val KEY_CRASH_REPORTING = "crash_reporting"
private const val KEY_DYNAMIC_COLOR = "dynamic_color"

@Singleton
class AppPreferences @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    var dashboardConfig: String?
        get() = sharedPreferences.getString(KEY_DASHBOARD_CONFIG, null)
        set(value) {
            sharedPreferences.edit().putString(KEY_DASHBOARD_CONFIG, value).apply()
        }

    var onboardingFirstHabitCreated: Boolean
        get() = sharedPreferences.getBoolean(KEY_ONBOARDING_FIRST_HABIT_CREATED, false)
        set(value) = sharedPreferences.edit().putBoolean(KEY_ONBOARDING_FIRST_HABIT_CREATED, value)
            .apply()

    var onboardingFirstActionCompleted: Boolean
        get() = sharedPreferences.getBoolean(KEY_ONBOARDING_FIRST_ACTION_COMPLETED, false)
        set(value) = sharedPreferences.edit()
            .putBoolean(KEY_ONBOARDING_FIRST_ACTION_COMPLETED, value).apply()

    var onboardingHabitDetailsOpened: Boolean
        get() = sharedPreferences.getBoolean(KEY_ONBOARDING_HABIT_DETAILS_OPENED, false)
        set(value) = sharedPreferences.edit().putBoolean(KEY_ONBOARDING_HABIT_DETAILS_OPENED, value)
            .apply()

    var onboardingInsightsOpened: Boolean
        get() = sharedPreferences.getBoolean(KEY_ONBOARDING_INSIGHTS_OPENED, false)
        set(value) = sharedPreferences.edit().putBoolean(KEY_ONBOARDING_INSIGHTS_OPENED, value)
            .apply()

    var crashReportingEnabled: Boolean
        get() = sharedPreferences.getBoolean(KEY_CRASH_REPORTING, true)
        set(value) = sharedPreferences.edit().putBoolean(KEY_CRASH_REPORTING, value).apply()

    // This preference is observable so that the app theme can be recomposed instantly
    // TODO: it would be nice to migrate to DataStore, which supports observability out of the box
    private val _dynamicColorEnabled =
        MutableStateFlow(sharedPreferences.getBoolean(KEY_DYNAMIC_COLOR, false))
    val dynamicColorEnabled: StateFlow<Boolean> = _dynamicColorEnabled

    fun setDynamicColorEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_DYNAMIC_COLOR, enabled).apply()
        _dynamicColorEnabled.value = enabled
    }


}