package com.eesuhn.habittracker.feature.dashboard.ui.dashboard.view.fiveday

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eesuhn.habittracker.core.model.Action
import com.eesuhn.habittracker.core.model.Habit
import com.eesuhn.habittracker.core.model.HabitId
import com.eesuhn.habittracker.core.model.HabitWithActions
import com.eesuhn.habittracker.feature.dashboard.ui.dashboard.ItemMoveEvent
import com.eesuhn.habittracker.feature.dashboard.ui.dashboard.view.DayLegend
import com.eesuhn.habittracker.feature.dashboard.ui.dashboard.view.ReorderableHabitList
import kotlinx.collections.immutable.ImmutableList
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import java.time.LocalDate

@Composable
fun FiveDayHabitList(
    habits: ImmutableList<HabitWithActions>,
    onActionToggle: (Action, Habit, Int) -> Unit,
    onHabitClick: (HabitId) -> Unit,
    onAddHabitClick: () -> Unit,
    onMove: (ItemMoveEvent) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.End
    ) {
        val width = Constants.CircleSize * 5 + Constants.CirclePadding * 8
        DayLegend(
            modifier = Modifier.wrapContentWidth(Alignment.End).width(width).padding(bottom = 12.dp, end = 120.dp),
            mostRecentDay = LocalDate.now(),
            pastDayCount = 4
        )

        ReorderableHabitList(
            habits, Arrangement.spacedBy(16.dp), onMove, onAddHabitClick
        ) { item, reorderState ->
            HabitCard(
                habit = item.habit,
                actions = item.actions,
                totalActionCount = item.totalActionCount,
                actionHistory = item.actionHistory,
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