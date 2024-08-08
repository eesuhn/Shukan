package com.eesuhn.habittracker.core.common

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

object OnboardingData {
    val steps: ImmutableList<Step> = persistentListOf(
        Step(
            index = 0,
            title = R.string.onboarding_step_create_title,
            subtitle = R.string.onboarding_step_create_subtitle
        ),
        Step(
            index = 1,
            title = R.string.onboarding_step_longpress_title,
            subtitle = R.string.onboarding_step_longpress_subtitle
        ),
        Step(
            index = 2,
            title = R.string.onboarding_step_details_title,
            subtitle = R.string.onboarding_step_details_subtitle
        ),
        Step(
            index = 3,
            title = R.string.onboarding_step_insights_title,
            subtitle = R.string.onboarding_step_insights_subtitle
        )
    )
    val totalSteps = steps.size
}

@Singleton
class OnboardingManager @Inject constructor(
    private val appPreferences: AppPreferences
) {
    private var firstHabitCreated by appPreferences::onboardingFirstHabitCreated
    private var firstActionCompleted by appPreferences::onboardingFirstActionCompleted
    private var habitDetailsOpened by appPreferences::onboardingHabitDetailsOpened
    private var insightsOpened by appPreferences::onboardingInsightsOpened

    /**
     * Current onboarding state or null if onboarding has been completed
     */
    val state = MutableStateFlow(currentState())

    fun firstHabitCreated() {
        if (!firstHabitCreated) {
            firstHabitCreated = true
        }
        state.value = currentState()
    }

    fun firstActionCompleted() {
        if (!firstActionCompleted) {
            firstActionCompleted = true
        }
        state.value = currentState()
    }

    fun habitDetailsOpened() {
        if (!habitDetailsOpened) {
            habitDetailsOpened = true
        }
        state.value = currentState()
    }

    fun insightsOpened() {
        if (!insightsOpened) {
            insightsOpened = true
        }
        state.value = currentState()
    }

    private fun currentState(): OnboardingState? {
        return null
    }
}