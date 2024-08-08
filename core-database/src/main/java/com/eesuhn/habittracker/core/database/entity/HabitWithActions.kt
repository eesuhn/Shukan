package com.eesuhn.habittracker.core.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class HabitWithActions(
    @Embedded val habit: Habit,

    @Relation(
        parentColumn = "id",
        entityColumn = "habit_id"
    )
    val actions: List<Action>
)