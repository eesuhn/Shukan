package com.eesuhn.habittracker.feature.dashboard.repo

import com.eesuhn.habittracker.core.database.HabitDao
import com.eesuhn.habittracker.core.model.Action
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import javax.inject.Inject
import javax.inject.Singleton
import com.eesuhn.habittracker.core.database.entity.Action as ActionEntity

@Singleton
class ActionRepository @Inject constructor(
    private val dao: HabitDao,
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun toggleAction(
        habitId: Int,
        updatedAction: Action,
        date: LocalDate,
    ) {
        withContext(dispatcher) {
            if (updatedAction.toggled) {
                val newAction = ActionEntity(
                    habit_id = habitId,
                    timestamp = LocalDateTime.of(date, LocalTime.now())
                        .toInstant(OffsetDateTime.now().offset)
                )
                dao.insertActions(listOf(newAction))
            } else {
                dao.deleteAction(updatedAction.id)
            }
        }
    }
}