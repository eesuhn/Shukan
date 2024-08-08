package com.eesuhn.habittracker.feature.insights.ui.component

import androidx.annotation.FloatRange
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.eesuhn.habittracker.core.model.HabitId
import com.eesuhn.habittracker.core.ui.theme.CoreIcons
import com.eesuhn.habittracker.core.ui.theme.LocalAppColors
import com.eesuhn.habittracker.core.ui.theme.PreviewTheme
import com.eesuhn.habittracker.feature.insights.R
import com.eesuhn.habittracker.feature.insights.model.TopHabitItem
import com.eesuhn.habittracker.feature.insights.ui.InsightsViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun TopHabits(viewModel: InsightsViewModel, navigateToHabitDetails: (HabitId) -> Unit) {
    val topHabits by viewModel.topHabits.collectAsState()
}

@Composable
private fun TopHabitsTable(
    habits: ImmutableList<TopHabitItem>,
    onHabitClick: (HabitId) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        habits.forEachIndexed { index, element ->
            TopHabitsRow(
                index = index + 1,
                item = element,
                onClick = onHabitClick
            )
        }
    }
}

@Composable
private fun TopHabitsRow(
    index: Int,
    item: TopHabitItem,
    onClick: (HabitId) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$index.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.width(24.dp)
        )

        Spacer(Modifier.width(16.dp))

        Text(
            text = item.name,
            modifier = Modifier.weight(0.50f),
            overflow = TextOverflow.Ellipsis,
            softWrap = false,
            style = MaterialTheme.typography.bodyMedium,
        )

        Text(
            text = item.count.toString(),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(0.2f)
        )

        Spacer(Modifier.width(16.dp))

        HabitBar(
            progress = item.progress,
            modifier = Modifier.weight(0.2f)
        )

        IconButton(onClick = { onClick(item.habitId) }) {
            Icon(
                painter = CoreIcons.ChevronRight,
                contentDescription = stringResource(
                    R.string.insights_tophabits_navigate,
                    item.name
                )
            )
        }
    }
}

@Composable
private fun HabitBar(
    @FloatRange(from = 0.0, to = 1.0) progress: Float,
    modifier: Modifier = Modifier,
) {
    val height = 8.dp
    val shape = RoundedCornerShape(4.dp)

    Box(
        modifier = modifier
            .clip(shape)
            .fillMaxWidth()
            .height(height)
            .background(LocalAppColors.current.gray1)
    ) {
        Box(
            modifier = Modifier
                .clip(shape)
                .fillMaxWidth(fraction = progress)
                .height(height)
                .background(MaterialTheme.colorScheme.tertiary)
        )
    }
}

@Preview
@ShowkaseComposable(name = "Top habits table", group = "Insights")
@Composable
fun PreviewTopHabitsTable() {
    val topHabits = persistentListOf(
        TopHabitItem(
            habitId = 1,
            name = "Short name",
            count = 1567,
            progress = 1f
        ),
        TopHabitItem(
            habitId = 1,
            name = "Name",
            count = 153,
            progress = 0.8f
        ),
        TopHabitItem(
            habitId = 1,
            name = "Loooong name lorem ipsum dolor sit amet",
            count = 10,
            progress = 0.5f
        ),
        TopHabitItem(
            habitId = 1,
            name = "Meditation",
            count = 9,
            progress = 0.1f
        ),
        TopHabitItem(
            habitId = 1,
            name = "Workout",
            count = 3,
            progress = 0f
        )
    )

    PreviewTheme {
        TopHabitsTable(
            habits = topHabits,
            onHabitClick = { }
        )
    }
}