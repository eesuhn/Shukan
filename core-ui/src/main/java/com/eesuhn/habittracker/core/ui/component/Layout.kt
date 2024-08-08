package com.eesuhn.habittracker.core.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints

@Composable
fun HorizontalGrid(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(content, modifier) { measurables, constraints ->
        val itemConstraints = Constraints.fixedWidth(constraints.maxWidth / measurables.size)
        val placeables = measurables.map {
            it.measure(itemConstraints)
        }

        val height = placeables.maxOf { it.height }
        layout(constraints.maxWidth, height) {
            var xOffset = 0

            placeables.forEach {
                it.placeRelative(x = xOffset, y = 0)
                xOffset += it.width
            }
        }
    }
}