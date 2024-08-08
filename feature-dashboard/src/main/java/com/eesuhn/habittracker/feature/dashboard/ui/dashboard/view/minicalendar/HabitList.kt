package com.eesuhn.habittracker.feature.dashboard.ui.dashboard.view.minicalendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eesuhn.habittracker.core.model.Action
import com.eesuhn.habittracker.core.model.Habit
import com.eesuhn.habittracker.core.model.HabitId
import com.eesuhn.habittracker.core.model.HabitWithActions
import com.eesuhn.habittracker.feature.dashboard.ui.dashboard.ItemMoveEvent
import com.eesuhn.habittracker.feature.dashboard.ui.dashboard.view.ReorderableHabitList
import kotlinx.collections.immutable.ImmutableList
import org.burnoutcrew.reorderable.detectReorderAfterLongPress

@Composable
fun MiniCalendarHabitList(
    habits: ImmutableList<HabitWithActions>,
    onActionToggle: (Action, Habit, Int) -> Unit,
    onHabitClick: (HabitId) -> Unit,
    onAddHabitClick: () -> Unit,
    onMove: (ItemMoveEvent) -> Unit
) {
    Column(horizontalAlignment = Alignment.End) {
        ReorderableHabitList(
            habits, Arrangement.spacedBy(16.dp), onMove, onAddHabitClick
        ) { item, reorderState ->
            HabitCard(
                habit = item.habit,
                actions = item.actions,
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