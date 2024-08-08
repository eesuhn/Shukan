package com.eesuhn.habittracker.feature.dashboard.ui.habitdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eesuhn.habittracker.core.common.OnboardingManager
import com.eesuhn.habittracker.core.common.Telemetry
import com.eesuhn.habittracker.core.database.HabitDao
import com.eesuhn.habittracker.core.model.Action
import com.eesuhn.habittracker.core.model.Habit
import com.eesuhn.habittracker.core.model.HabitWithActions
import com.eesuhn.habittracker.core.ui.state.Result
import com.eesuhn.habittracker.feature.dashboard.mapper.actionsToHistory
import com.eesuhn.habittracker.feature.dashboard.mapper.mapActionCountByMonth
import com.eesuhn.habittracker.feature.dashboard.mapper.mapActionCountByMonthListToItemList
import com.eesuhn.habittracker.feature.dashboard.mapper.mapActionCountByWeek
import com.eesuhn.habittracker.feature.dashboard.mapper.mapActionCountByWeekListToItemList
import com.eesuhn.habittracker.feature.dashboard.mapper.mapHabitSingleStats
import com.eesuhn.habittracker.feature.dashboard.mapper.toEntity
import com.eesuhn.habittracker.feature.dashboard.mapper.toUIColor
import com.eesuhn.habittracker.feature.dashboard.repo.ActionRepository
import com.eesuhn.habittracker.feature.dashboard.ui.model.ActionCountByMonth
import com.eesuhn.habittracker.feature.dashboard.ui.model.ActionCountByWeek
import com.eesuhn.habittracker.feature.dashboard.ui.model.ActionCountChart
import com.eesuhn.habittracker.feature.dashboard.ui.model.SingleStats
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Locale
import javax.inject.Inject

private val initialSingleStats = SingleStats(null, 0, 0, 0f)
private val initialChartData = ActionCountChart(persistentListOf(), ActionCountChart.Type.Weekly)

enum class HabitDetailEvent {
    BackNavigation
}

@HiltViewModel
class HabitDetailViewModel @Inject constructor(
    private val dao: HabitDao,
    private val actionRepository: ActionRepository,
    private val telemetry: Telemetry,
    onboardingManager: OnboardingManager
) : ViewModel() {

    val habitWithActions = MutableStateFlow<Result<HabitWithActions>>(Result.Loading)
    val singleStats = MutableStateFlow(initialSingleStats)
    val chartData = MutableStateFlow<Result<ActionCountChart>>(Result.Success(initialChartData))

    private val eventChannel = Channel<HabitDetailEvent>(Channel.BUFFERED)
    val habitDetailEvent = eventChannel.receiveAsFlow()

    private val actionCountByWeek = MutableStateFlow<List<ActionCountByWeek>>(emptyList())
    private val actionCountByMonth = MutableStateFlow<List<ActionCountByMonth>>(emptyList())

    // Store loaded habit's order to convert the model back to entity later
    private var habitOrder: Int? = null

    init {
        onboardingManager.habitDetailsOpened()
    }

    fun fetchHabitDetails(habitId: Int): Job {
        return viewModelScope.launch {
            try {
                val habit = dao.getHabitWithActions(habitId).let {
                    habitOrder = it.habit.order

                    // TODO: unify this with the regular mapping (where empty day action are filled)
                    HabitWithActions(
                        Habit(
                            it.habit.id,
                            it.habit.name,
                            it.habit.color.toUIColor(),
                            it.habit.notes
                        ),
                        it.actions.map { action ->
                            Action(action.id, toggled = true, timestamp = action.timestamp)
                        }.toImmutableList(),
                        it.actions.size,
                        actionsToHistory(it.actions)
                    )
                }
                habitWithActions.value = Result.Success(habit)
            } catch (e: Throwable) {
                telemetry.logNonFatal(e)
                habitWithActions.value = Result.Failure(e)
            }
        }
    }

    fun fetchHabitStats(habitId: Int): Job {
        return viewModelScope.launch {
            try {
                val completionRate = async { dao.getCompletionRate(habitId) }
                val actionCountByWeekEntity = async { dao.getActionCountByWeek(habitId) }
                val actionCountByMonthEntity = async { dao.getActionCountByMonth(habitId) }

                singleStats.value = mapHabitSingleStats(
                    completionRate.await(),
                    actionCountByWeekEntity.await(),
                    LocalDate.now(),
                )
                actionCountByWeek.value = mapActionCountByWeek(actionCountByWeekEntity.await())
                actionCountByMonth.value = mapActionCountByMonth(actionCountByMonthEntity.await())
                if (chartData.value is Result.Success) {
                    switchChartType((chartData.value as Result.Success<ActionCountChart>).value.type)
                }
            } catch (e: Throwable) {
                // Fail silently
                telemetry.logNonFatal(e)
            }
        }
    }

    fun toggleAction(habitId: Int, action: Action, date: LocalDate) {
        viewModelScope.launch {
            actionRepository.toggleAction(habitId, action, date)
            fetchHabitDetails(habitId)
            fetchHabitStats(habitId)
        }
    }

    fun updateHabit(habit: Habit) {
        viewModelScope.launch {
            dao.updateHabit(habit.toEntity(order = habitOrder ?: 0, archived = false))
            fetchHabitDetails(habit.id)
        }
    }

    fun archiveHabit(habit: Habit) {
        viewModelScope.launch {
            dao.updateHabit(habit.toEntity(order = habitOrder ?: 0, archived = true))
            eventChannel.send(HabitDetailEvent.BackNavigation)
        }
    }

    fun switchChartType(newType: ActionCountChart.Type) {
        try {
            val items = when (newType) {
                ActionCountChart.Type.Weekly -> mapActionCountByWeekListToItemList(
                    actionCountByWeek.value,
                    LocalDate.now(),
                    Locale.getDefault()
                )

                ActionCountChart.Type.Monthly -> mapActionCountByMonthListToItemList(
                    actionCountByMonth.value,
                    LocalDate.now()
                )
            }
            chartData.value = Result.Success(ActionCountChart(items, newType))
        } catch (e: Throwable) {
            chartData.value = Result.Failure(e)
            telemetry.logNonFatal(e)
        }
    }
}