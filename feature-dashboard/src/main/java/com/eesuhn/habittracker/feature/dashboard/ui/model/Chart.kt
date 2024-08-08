package com.eesuhn.habittracker.feature.dashboard.ui.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import java.time.Year
import java.time.YearMonth

@Immutable
data class ActionCountByWeek(
    val year: Year,
    val weekOfYear: Int,
    val actionCount: Int
)

@Immutable
data class ActionCountByMonth(
    val yearMonth: YearMonth,
    val actionCount: Int
)

data class ActionCountChart(
    val items: ImmutableList<ChartItem>,
    val type: Type
) {
    data class ChartItem(
        val label: String,
        val year: Int,
        val value: Int
    )

    enum class Type {
        Weekly, Monthly;

        fun invert() = if (this == Weekly) Monthly else Weekly
    }
}