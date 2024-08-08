package com.eesuhn.habittracker.feature.misc.archive.model

import androidx.compose.runtime.Immutable
import com.eesuhn.habittracker.core.model.HabitId
import java.time.Instant

@Immutable
data class ArchivedHabit(
    val id: HabitId,
    val name: String,
    val totalActionCount: Int,
    val lastAction: Instant?
)