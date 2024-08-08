package com.eesuhn.habittracker.core.ui.recomposition

import androidx.compose.runtime.Stable

@Stable
class StableHolder<T>(val item: T) {
    operator fun component1(): T = item
}

