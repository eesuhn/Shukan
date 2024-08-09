package com.eesuhn.habittracker.feature.dashboard.ui.dashboard

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.eesuhn.habittracker.core.common.OnboardingState
import com.eesuhn.habittracker.core.model.Action
import com.eesuhn.habittracker.core.model.Habit
import com.eesuhn.habittracker.core.model.HabitId
import com.eesuhn.habittracker.core.model.HabitWithActions
import com.eesuhn.habittracker.core.ui.component.AppDefaultAppBar
import com.eesuhn.habittracker.core.ui.component.ContentWithPlaceholder
import com.eesuhn.habittracker.core.ui.component.ErrorView
import com.eesuhn.habittracker.core.ui.state.Result
import com.eesuhn.habittracker.core.ui.state.asEffect
import com.eesuhn.habittracker.core.ui.theme.AppTextStyle
import com.eesuhn.habittracker.core.ui.theme.CoreIcons
import com.eesuhn.habittracker.feature.dashboard.R
import com.eesuhn.habittracker.feature.dashboard.ui.DashboardIcons
import com.eesuhn.habittracker.feature.dashboard.ui.dashboard.view.compact.CompactHabitList
import com.eesuhn.habittracker.feature.dashboard.ui.dashboard.view.fiveday.FiveDayHabitList
import com.eesuhn.habittracker.feature.dashboard.ui.dashboard.view.minicalendar.MiniCalendarHabitList
import com.eesuhn.habittracker.feature.dashboard.ui.model.DashboardConfig
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import com.eesuhn.habittracker.core.ui.R as coreR

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    snackbarHostState: SnackbarHostState,
    navigateToDetails: (HabitId) -> Unit,
    navigateToAddHabit: () -> Unit,
    navigateToSettings: () -> Unit,
    navigateToArchive: () -> Unit,
    navigateToExport: () -> Unit
) {
    var config by remember { mutableStateOf(viewModel.dashboardConfig) }
    var configDialogOpen by remember { mutableStateOf(false) }
    val habits by viewModel.habitsWithActions.collectAsState(Result.Loading)
    val onboardingState by viewModel.onboardingState.collectAsState()

    val snackbarCoroutineScope = rememberCoroutineScope()
    DisplaySnackbarEvents(viewModel.dashboardEvent, snackbarCoroutineScope, snackbarHostState)

    val onActionToggle: (Action, Habit, Int) -> Unit = { action, habit, daysInPast ->
        viewModel.toggleAction(habit.id, action, daysInPast)
    }
    val onConfigClick: () -> Unit = { configDialogOpen = true }
    val onConfigChange: (DashboardConfig) -> Unit = {
        config = it
        viewModel.dashboardConfig = it
    }
    val onMove: (ItemMoveEvent) -> Unit = { viewModel.persistItemMove(it) }

    DashboardConfigDialog(
        isVisible = configDialogOpen,
        config = config,
        onConfigSelected = onConfigChange,
        onDismissed = { configDialogOpen = false }
    )

    when (habits) {
        is Result.Success -> {
            LoadedDashboard(
                habits = (habits as Result.Success<ImmutableList<HabitWithActions>>).value,
                config,
                onboardingState,
                navigateToAddHabit,
                onConfigClick,
                onActionToggle,
                navigateToDetails,
                navigateToSettings,
                navigateToArchive,
                navigateToExport,
                onMove
            )
        }

        is Result.Failure -> ErrorView(label = stringResource(R.string.dashboard_error))
        Result.Loading -> {}
    }
}

@Composable
private fun DisplaySnackbarEvents(
    dashboardEvent: Flow<DashboardEvent>,
    snackbarCoroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    val errorToggleAction = stringResource(R.string.dashboard_error_toggle_action)
    val errorItemMove = stringResource(R.string.dashboard_error_item_move)
    val eventAction = stringResource(R.string.dashboard_event_action_performed)
    dashboardEvent.asEffect {
        val errorMessage = when (it) {
            DashboardEvent.ToggleActionError -> errorToggleAction
            DashboardEvent.MoveHabitError -> errorItemMove
            DashboardEvent.ActionPerformed -> eventAction
        }
        snackbarCoroutineScope.launch {
            snackbarHostState.showSnackbar(message = errorMessage)
        }
    }
}

@Composable
private fun LoadedDashboard(
    habits: ImmutableList<HabitWithActions>,
    config: DashboardConfig,
    onboardingState: OnboardingState?,
    onAddHabitClick: () -> Unit,
    onConfigClick: () -> Unit,
    onActionToggle: (Action, Habit, Int) -> Unit,
    onHabitDetail: (HabitId) -> (Unit),
    onSettingsClick: () -> Unit,
    onArchiveClick: () -> Unit,
    onExportClick: () -> Unit,
    onMove: (ItemMoveEvent) -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)) {
        DashboardAppBar(onConfigClick, onSettingsClick, onArchiveClick, onExportClick)

        if (onboardingState != null) {
            Onboarding(onboardingState)
        }

        ContentWithPlaceholder(
            showPlaceholder = habits.isEmpty(),
            placeholder = { DashboardPlaceholder(onAddHabitClick) }
        ) {
            Crossfade(targetState = config) {
                when (it) {
                    DashboardConfig.FiveDay -> {
                        FiveDayHabitList(
                            habits, onActionToggle, onHabitDetail, onAddHabitClick, onMove
                        )
                    }

                    DashboardConfig.MiniCalendar -> {
                        MiniCalendarHabitList(
                            habits, onActionToggle, onHabitDetail, onAddHabitClick, onMove
                        )
                    }

                    DashboardConfig.Compact -> {
                        CompactHabitList(
                            habits, onActionToggle, onHabitDetail, onAddHabitClick, onMove
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DashboardAppBar(
    onConfigClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onArchiveClick: () -> Unit,
    onExportClick: () -> Unit
) {
    AppDefaultAppBar(
        navigationIcon = {
            IconButton(onClick = onConfigClick) {
                Icon(
                    DashboardIcons.DashboardLayout,
                    stringResource(R.string.dashboard_change_layout)
                )
            }
        },
        title = {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.dashboard_title),
                    style = AppTextStyle.screenTitle.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        },
        actions = {
            IconButton(onClick = onArchiveClick) {
                Icon(
                    painter = CoreIcons.Archive,
                    contentDescription = stringResource(coreR.string.menu_archive)
                )
            }
        }
    )
}

@Composable
private fun DashboardPlaceholder(onAddHabitClick: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .wrapContentWidth()
            .padding(top = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.padding(top = 2.dp))

        Image(
            painter = painterResource(R.drawable.illustration_empty_state),
            contentDescription = null
        )

        Button(
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp),
            onClick = onAddHabitClick
        ) {
            Icon(Icons.Rounded.Add, null, Modifier.size(ButtonDefaults.IconSize))
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(stringResource(R.string.dashboard_create_habit_first))
        }
    }
}
