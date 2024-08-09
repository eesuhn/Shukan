package com.eesuhn.habittracker.feature.insights.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.eesuhn.habittracker.core.model.HabitId
import com.eesuhn.habittracker.core.ui.component.AppDefaultRootAppBar
import com.eesuhn.habittracker.core.ui.theme.AppTextStyle
import com.eesuhn.habittracker.core.ui.theme.CoreIcons
import com.eesuhn.habittracker.feature.insights.ui.component.Heatmap
import com.eesuhn.habittracker.feature.insights.ui.component.TopDays
import com.eesuhn.habittracker.feature.insights.ui.component.TopHabits
import com.eesuhn.habittracker.core.ui.R as coreR
import com.eesuhn.habittracker.feature.insights.R as insightsR

@Composable
fun InsightsScreen(
    viewModel: InsightsViewModel,
    navigateToSettings: () -> Unit,
    navigateToArchive: () -> Unit,
    navigateToExport: () -> Unit,
    navigateToHabitDetails: (HabitId) -> Unit
) {
    Column(Modifier.background(MaterialTheme.colorScheme.background)) {
        InsightsAppBar(
            onSettingsClick = navigateToSettings,
            onArchiveClick = navigateToArchive,
            onExportClick = navigateToExport
        )

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp, bottom = 32.dp, top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Heatmap(viewModel)

            TopHabits(viewModel, navigateToHabitDetails)

            TopDays(viewModel, navigateToHabitDetails)
        }
    }
}

@Composable
private fun InsightsAppBar(
    onSettingsClick: () -> Unit,
    onArchiveClick: () -> Unit,
    onExportClick: () -> Unit
) {
    AppDefaultRootAppBar(
        title = {
            Text(
                text = stringResource(insightsR.string.insights_screen_title),
                style = AppTextStyle.screenTitle.copy(fontWeight = FontWeight.Bold)
            )
        },
        actions = {
            IconButton(onClick = onArchiveClick) {
                Icon(
                    painter = CoreIcons.Archive,
                    contentDescription = stringResource(coreR.string.menu_archive)
                )
            }
        },
    )
}