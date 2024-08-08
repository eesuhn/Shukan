package com.eesuhn.habittracker.core.database.entity

import androidx.room.Embedded
import java.time.Instant

data class HabitDayView(
    @Embedded val habit: Habit,
    val timestamp: Instant?,
)