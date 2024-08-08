package com.eesuhn.habittracker.feature.insights.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.eesuhn.habittracker.feature.insights.R as insightsR

object InsightsIcons {
    val Heatmap: Painter
        @Composable
        get() = painterResource(insightsR.drawable.ic_heatmap)

}