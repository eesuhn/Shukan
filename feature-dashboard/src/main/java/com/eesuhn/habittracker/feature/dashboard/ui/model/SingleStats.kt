package com.eesuhn.habittracker.feature.dashboard.ui.model

import androidx.annotation.FloatRange
import androidx.compose.runtime.Immutable
import java.time.LocalDate

@Immutable
data class SingleStats(
    val firstDay: LocalDate?,
    val actionCount: Int,
    val weeklyActionCount: Int,
    @FloatRange(from = 0.0, to = 1.0) val completionRate: Float,
)