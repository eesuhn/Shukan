package com.eesuhn.habittracker.core.model

import androidx.compose.runtime.Immutable

@Immutable
sealed class ActionHistory {

    object Clean : ActionHistory()

    data class Streak(val days: Int) : ActionHistory()

    data class MissedDays(val days: Int) : ActionHistory()
}
