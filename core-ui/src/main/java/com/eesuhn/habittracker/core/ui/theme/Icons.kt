package com.eesuhn.habittracker.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.eesuhn.habittracker.core.ui.R

object CoreIcons {

    val Habits: Painter
        @Composable
        get() = painterResource(R.drawable.ic_habit)

    val Error: Painter
        @Composable
        get() = painterResource(R.drawable.ic_error)

    val Archive: Painter
        @Composable
        get() = painterResource(R.drawable.ic_archive)

    val ChevronLeft: Painter
        @Composable
        get() = painterResource(R.drawable.ic_chevron_left)

    val ChevronRight: Painter
        @Composable
        get() = painterResource(R.drawable.ic_chevron_right)

    val InfoOutlined: Painter
        @Composable
        get() = painterResource(R.drawable.ic_info_outlined)
}

