package com.eesuhn.habittracker.core.ui.theme

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut

/**
 * Navigation transitions mirroring the Material spec and the non-Compose Material Design Components
 */
object AppTransition {
    val defaultEnter = fadeIn()
    val defaultExit = fadeOut()

    val fadeThroughEnter = fadeIn(
        initialAlpha = 0.35f,
        animationSpec = tween(durationMillis = 210, delayMillis = 90)
    ) + scaleIn(
        initialScale = 0.92f,
        animationSpec = tween(durationMillis = 210, delayMillis = 90)
    )
    val fadeThroughExit = fadeOut(animationSpec = tween(durationMillis = 90))

    val sharedZAxisEnterForward = scaleIn(
        initialScale = 0.8f,
        animationSpec = tween(300)
    ) + fadeIn(animationSpec = tween(durationMillis = 60, delayMillis = 60, easing = LinearEasing))
    val sharedZAxisEnterBackward = scaleIn(
        initialScale = 1.1f,
        animationSpec = tween(300)
    )
    val sharedZAxisExitForward = scaleOut(
        targetScale = 1.1f,
        animationSpec = tween(durationMillis = 300),
    )
    val sharedZAxisExitBackward = scaleOut(
        targetScale = 0.8f,
        animationSpec = tween(durationMillis = 300),
    ) + fadeOut(animationSpec = tween(durationMillis = 60, delayMillis = 60, easing = LinearEasing))
}