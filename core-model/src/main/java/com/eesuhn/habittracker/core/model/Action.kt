package com.eesuhn.habittracker.core.model

import androidx.compose.runtime.Immutable
import java.time.Instant

@Immutable
data class Action(
    val id: Int,
    val toggled: Boolean,
    val timestamp: Instant?
)