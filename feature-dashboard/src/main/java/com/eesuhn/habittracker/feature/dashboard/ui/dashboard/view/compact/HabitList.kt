package com.eesuhn.habittracker.feature.dashboard.ui.dashboard.view.compact

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.eesuhn.habittracker.core.model.Action
import com.eesuhn.habittracker.core.model.Habit
import com.eesuhn.habittracker.core.model.HabitId
import com.eesuhn.habittracker.core.model.HabitWithActions
import com.eesuhn.habittracker.feature.dashboard.ui.dashboard.ItemMoveEvent
import com.eesuhn.habittracker.feature.dashboard.ui.dashboard.view.DayLegend
import com.eesuhn.habittracker.feature.dashboard.ui.dashboard.view.ReorderableHabitList
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import java.time.LocalDate

@Composable
fun CompactHabitList(
    habits: ImmutableList<HabitWithActions>,
    onActionToggle: (Action, Habit, Int) -> Unit,
    onHabitClick: (HabitId) -> Unit,
    onAddHabitClick: () -> Unit,
    onMove: (ItemMoveEvent) -> Unit,
) {
    Column {
        DayLegend(
            modifier = Modifier.fillMaxWidth(),
            mostRecentDay = LocalDate.now(),
            pastDayCount = Constants.DAY_COUNT - 1
        )

        ReorderableHabitList(
            habits, Arrangement.Top, onMove, onAddHabitClick
        ) { item, reorderState ->
            HabitItem(
                habit = item.habit,
                actions = item.actions.takeLast(Constants.DAY_COUNT).toImmutableList(),
                onActionToggle = onActionToggle,
                onDetailClick = onHabitClick,
                // Null and 0 drag offset is intentionally treated as the same because dragging
                // is using the same gesture detection as the long-press Action toggle modifier
                dragOffset = reorderState.offsetByKey(item.habit.id)?.y?.toFloat() ?: 0f,
                modifier = Modifier.detectReorderAfterLongPress(reorderState)
            )
        }
    }
}