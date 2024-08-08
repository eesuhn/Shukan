package com.eesuhn.habittracker.core.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class HabitWithActions(
    val habit: Habit,
    val actions: ImmutableList<Action>,
    val totalActionCount: Int,
    val actionHistory: ActionHistory
)