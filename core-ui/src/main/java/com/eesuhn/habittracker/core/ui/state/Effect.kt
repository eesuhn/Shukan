package com.eesuhn.habittracker.core.ui.state

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow

@SuppressLint("ComposableNaming")
@Composable
fun <T> Flow<T>.asEffect(action: (T) -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleAwareFlow = remember(this, lifecycleOwner) {
        this.flowWithLifecycle(lifecycleOwner.lifecycle)
    }

    // Make sure the LaunchedEffect lambda refers to the latest `action` value,
    // even if a recomposition happens with a new `action`
    val currentAction by rememberUpdatedState(action)

    LaunchedEffect(this, lifecycleOwner) {
        lifecycleAwareFlow.collect { currentAction(it) }
    }
}