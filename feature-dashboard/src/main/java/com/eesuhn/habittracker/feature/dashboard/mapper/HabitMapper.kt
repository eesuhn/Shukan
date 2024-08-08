package com.eesuhn.habittracker.feature.dashboard.mapper

import com.eesuhn.habittracker.core.model.Habit
import com.eesuhn.habittracker.core.model.HabitWithActions
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import com.eesuhn.habittracker.core.database.entity.Habit as HabitEntity
import com.eesuhn.habittracker.core.database.entity.HabitWithActions as HabitWithActionsEntity

fun mapHabitEntityToModel(habitsWithActions: List<HabitWithActionsEntity>): ImmutableList<HabitWithActions> {
    return habitsWithActions.map {
        HabitWithActions(
            habit = Habit(it.habit.id, it.habit.name, it.habit.color.toUIColor(), it.habit.notes),
            actions = actionsToRecentDays(it.actions),
            totalActionCount = it.actions.size,
            actionHistory = actionsToHistory(it.actions)
        )
    }.toImmutableList()
}

fun Habit.toEntity(order: Int, archived: Boolean) = HabitEntity(
    id = this.id,
    name = this.name,
    color = this.color.toEntityColor(),
    order = order,
    archived = archived,
    notes = this.notes
)

fun HabitEntity.Color.toUIColor(): Habit.Color = when (this) {
    HabitEntity.Color.Red -> Habit.Color.Red
    HabitEntity.Color.Green -> Habit.Color.Green
    HabitEntity.Color.Blue -> Habit.Color.Blue
    HabitEntity.Color.Yellow -> Habit.Color.Yellow
    HabitEntity.Color.Cyan -> Habit.Color.Cyan
    HabitEntity.Color.Pink -> Habit.Color.Pink
}

fun Habit.Color.toEntityColor(): HabitEntity.Color = when (this) {
    Habit.Color.Red -> HabitEntity.Color.Red
    Habit.Color.Green -> HabitEntity.Color.Green
    Habit.Color.Blue -> HabitEntity.Color.Blue
    Habit.Color.Yellow -> HabitEntity.Color.Yellow
    Habit.Color.Cyan -> HabitEntity.Color.Cyan
    Habit.Color.Pink -> HabitEntity.Color.Pink
}