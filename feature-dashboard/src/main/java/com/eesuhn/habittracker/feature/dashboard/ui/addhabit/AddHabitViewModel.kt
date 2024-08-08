package com.eesuhn.habittracker.feature.dashboard.ui.addhabit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eesuhn.habittracker.core.common.OnboardingManager
import com.eesuhn.habittracker.core.common.Telemetry
import com.eesuhn.habittracker.core.database.HabitDao
import com.eesuhn.habittracker.core.model.Habit
import com.eesuhn.habittracker.feature.dashboard.mapper.toEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddHabitViewModel @Inject constructor(
    private val dao: HabitDao,
    private val onboardingManager: OnboardingManager,
    private val telemetry: Telemetry
) : ViewModel() {

    private val backNavigationEventChannel = Channel<Unit>(Channel.BUFFERED)
    val backNavigationEvent: Flow<Unit> = backNavigationEventChannel.receiveAsFlow()

    fun addHabit(habit: Habit) {
        viewModelScope.launch {
            try {
                val habitCount = dao.getTotalHabitCount().first()
                val habitEntity = habit.toEntity(order = habitCount, archived = false)
                dao.insertHabits(listOf(habitEntity))
                onboardingManager.firstHabitCreated()
                backNavigationEventChannel.send(Unit)
            } catch (e: Throwable) {
                // TODO: error handling on UI
                telemetry.logNonFatal(e)
            }
        }
    }
}