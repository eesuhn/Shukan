package com.eesuhn.habittracker.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.eesuhn.habittracker.R
import com.eesuhn.habittracker.core.ui.theme.CoreIcons

@Composable
fun AppNavigationBar(navController: NavController) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        AppNavigationItem(
            rootScreen = Destination.Dashboard,
            content = { Icon(CoreIcons.Habits, stringResource(R.string.tab_dashboard)) },
            label = stringResource(R.string.tab_dashboard),
            currentDestination = currentDestination,
            navController = navController
        )
        AppNavigationItem(
            rootScreen = Destination.Insights,
            content = { Icon(AppIcons.Insights, stringResource(R.string.tab_insights)) },
            label = stringResource(R.string.tab_insights),
            currentDestination = currentDestination,
            navController = navController
        )
    }
}

@Composable
private fun RowScope.AppNavigationItem(
    rootScreen: Screen,
    label: String,
    currentDestination: NavDestination?,
    navController: NavController,
    content: @Composable () -> Unit
) {
    NavigationBarItem(
        selected = currentDestination?.hierarchy?.any { it.route == rootScreen.route } == true,
        onClick = {
            navController.navigate(rootScreen.route) {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                popUpTo(navController.graph.findStartDestination().id)
                // Avoid multiple copies of the same destination when
                // re-selecting the same item
                launchSingleTop = true
            }
        },
        label = { Text(label) },
        icon = content,
    )
}
