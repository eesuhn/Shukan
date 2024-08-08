package com.eesuhn.habittracker.core.common

typealias StringRes = Int

data class Step(
    val index: Int,
    val title: StringRes,
    val subtitle: StringRes
)

data class OnboardingState(
    val step: Step,
    val totalSteps: Int
)