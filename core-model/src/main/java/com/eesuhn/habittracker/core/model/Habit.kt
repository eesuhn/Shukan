package com.eesuhn.habittracker.core.model

import androidx.compose.runtime.Immutable

typealias HabitId = Int

@Immutable
data class Habit(
    val id: HabitId = 0,
    val name: String,
    val color: Color,
    val notes: String
) {

    companion object {
        val DEFAULT_COLOR = Color.Yellow
    }

    enum class Color {
        // Note: enum order determines order on UI!

        Yellow,
        Green,
        Blue,
        Red,
        Cyan,
        Pink,
    }
}