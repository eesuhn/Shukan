package com.eesuhn.habittracker.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

typealias HabitId = Int

@Entity
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val color: Color,
    val order: Int,
    val archived: Boolean,
    val notes: String
) {

    enum class Color {
        Red,
        Green,
        Blue,
        Yellow,
        Cyan,
        Pink,
    }

}

/**
 * Partial Habit class to allow deleting by ID only
 */
data class HabitById(
    val id: Int
)