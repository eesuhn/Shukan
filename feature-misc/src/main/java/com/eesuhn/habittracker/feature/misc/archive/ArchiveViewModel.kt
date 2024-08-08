package com.eesuhn.habittracker.feature.misc.archive

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eesuhn.habittracker.core.common.Telemetry
import com.eesuhn.habittracker.core.database.HabitDao
import com.eesuhn.habittracker.core.database.entity.HabitById
import com.eesuhn.habittracker.core.ui.state.Result
import com.eesuhn.habittracker.feature.misc.archive.model.ArchivedHabit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.eesuhn.habittracker.core.database.entity.HabitWithActions as HabitWithActionsEntity

enum class ArchiveEvent {
    UnarchiveError,
    DeleteError
}

@HiltViewModel
class ArchiveViewModel @Inject constructor(
    private val dao: HabitDao,
    private val telemetry: Telemetry
) : ViewModel() {

    private val eventChannel = Channel<ArchiveEvent>(Channel.BUFFERED)
    val archiveEvent = eventChannel.receiveAsFlow()

    val archivedHabitList: Flow<Result<ImmutableList<ArchivedHabit>>> = dao
        .getArchivedHabitsWithActions()
        .map<List<HabitWithActionsEntity>, Result<ImmutableList<ArchivedHabit>>> {
            Result.Success(mapHabitEntityToArchivedModel(it))
        }
        .catch {
            telemetry.logNonFatal(it)
            emit(Result.Failure(it))
        }

    fun unarchiveHabit(habit: ArchivedHabit) {
        viewModelScope.launch {
            try {
                dao.unarchiveHabit(habit.id)
            } catch (e: Throwable) {
                telemetry.logNonFatal(e)
                eventChannel.send(ArchiveEvent.UnarchiveError)
            }
        }
    }

    fun deleteHabit(habit: ArchivedHabit) {
        viewModelScope.launch {
            try {
                dao.deleteHabit(HabitById(habit.id))
            } catch (e: Throwable) {
                telemetry.logNonFatal(e)
                eventChannel.send(ArchiveEvent.DeleteError)
            }
        }
    }

    private fun mapHabitEntityToArchivedModel(habitsWithActions: List<HabitWithActionsEntity>): ImmutableList<ArchivedHabit> {
        return habitsWithActions.map {
            ArchivedHabit(
                id = it.habit.id,
                name = it.habit.name,
                totalActionCount = it.actions.size,
                lastAction = it.actions.lastOrNull()?.timestamp
            )
        }.toImmutableList()
    }
}