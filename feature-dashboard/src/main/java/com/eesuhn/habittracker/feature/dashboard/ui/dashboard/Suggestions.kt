package com.eesuhn.habittracker.feature.dashboard.ui.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import com.eesuhn.habittracker.core.common.R as commonR

object Suggestions {

    val habits: ImmutableList<String>
        @Composable get() = persistentListOf(
            stringResource(commonR.string.habit_suggestion_reading),
            stringResource(commonR.string.habit_suggestion_workout),
            stringResource(commonR.string.habit_suggestion_meditation),
            stringResource(commonR.string.habit_suggestion_walk),
            stringResource(commonR.string.habit_suggestion_plan_my_day),
            stringResource(commonR.string.habit_suggestion_stretch),
            stringResource(commonR.string.habit_suggestion_go_to_bed),
            stringResource(commonR.string.habit_suggestion_journal),
            stringResource(commonR.string.habit_suggestion_spend_time),
        )
}