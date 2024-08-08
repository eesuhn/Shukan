package com.eesuhn.habittracker.core.model

import androidx.compose.runtime.Immutable

@Immutable
data class HabitDayView(
    val habit: Habit,
    val toggled: Boolean,
)