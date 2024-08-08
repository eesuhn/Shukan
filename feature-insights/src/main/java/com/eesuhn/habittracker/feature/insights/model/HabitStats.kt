package com.eesuhn.habittracker.feature.insights.model

import androidx.annotation.FloatRange
import androidx.compose.runtime.Immutable
import com.eesuhn.habittracker.core.model.HabitId
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import java.time.LocalDate
import java.time.YearMonth

typealias BucketIndex = Int

@Immutable
data class HeatmapMonth(
    val yearMonth: YearMonth,
    val dayMap: ImmutableMap<LocalDate, BucketInfo>,
    val totalHabitCount: Int,
    val bucketCount: Int,
    val bucketMaxValues: ImmutableList<Pair<BucketIndex, Int>>
) {
    data class BucketInfo(
        val bucketIndex: BucketIndex,
        val value: Int // Actual value (habit count on day) that bucketing is based on
    )
}

data class TopHabitItem(
    val habitId: HabitId,
    val name: String,
    val count: Int,
    @FloatRange(from = 0.0, to = 1.0) val progress: Float
)

data class TopDayItem(
    val habitId: HabitId,
    val name: String,
    val dayLabel: String,
    val count: Int
)