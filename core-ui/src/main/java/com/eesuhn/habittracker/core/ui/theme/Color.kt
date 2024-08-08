package com.eesuhn.habittracker.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.eesuhn.habittracker.core.model.Habit

data class AppColors(
    val successContainer: Color,
    val gray1: Color,
    val gray2: Color,
    val habitRed: Color,
    val habitRedContainer: Color,
    val onHabitRedContainer: Color,
    val habitGreen: Color,
    val habitGreenContainer: Color,
    val onHabitGreenContainer: Color,
    val habitBlue: Color,
    val habitBlueContainer: Color,
    val onHabitBlueContainer: Color,
    val habitYellow: Color,
    val habitYellowContainer: Color,
    val onHabitYellowContainer: Color,
    val habitCyan: Color,
    val habitCyanContainer: Color,
    val onHabitCyanContainer: Color,
    val habitPink: Color,
    val habitPinkContainer: Color,
    val onHabitPinkContainer: Color,
)

val Habit.Color.composeColor: Color
    @Composable
    @ReadOnlyComposable
    get() = when (this) {
        Habit.Color.Green -> LocalAppColors.current.habitGreen
        Habit.Color.Blue -> LocalAppColors.current.habitBlue
        Habit.Color.Yellow -> LocalAppColors.current.habitYellow
        Habit.Color.Red -> LocalAppColors.current.habitRed
        Habit.Color.Cyan -> LocalAppColors.current.habitCyan
        Habit.Color.Pink -> LocalAppColors.current.habitPink
    }

val Habit.Color.composeContainerColor: Color
    @Composable
    @ReadOnlyComposable
    get() = when (this) {
        Habit.Color.Green -> LocalAppColors.current.habitGreenContainer
        Habit.Color.Blue -> LocalAppColors.current.habitBlueContainer
        Habit.Color.Yellow -> LocalAppColors.current.habitYellowContainer
        Habit.Color.Red -> LocalAppColors.current.habitRedContainer
        Habit.Color.Cyan -> LocalAppColors.current.habitCyanContainer
        Habit.Color.Pink -> LocalAppColors.current.habitPinkContainer
    }

val Habit.Color.composeOnContainerColor: Color
    @Composable
    @ReadOnlyComposable
    get() = when (this) {
        Habit.Color.Green -> LocalAppColors.current.onHabitGreenContainer
        Habit.Color.Blue -> LocalAppColors.current.onHabitBlueContainer
        Habit.Color.Yellow -> LocalAppColors.current.onHabitYellowContainer
        Habit.Color.Red -> LocalAppColors.current.onHabitRedContainer
        Habit.Color.Cyan -> LocalAppColors.current.onHabitCyanContainer
        Habit.Color.Pink -> LocalAppColors.current.onHabitPinkContainer
    }
