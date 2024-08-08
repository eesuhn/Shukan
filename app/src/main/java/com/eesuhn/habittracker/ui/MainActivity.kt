package com.eesuhn.habittracker.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.eesuhn.habittracker.core.common.AppPreferences
import com.eesuhn.habittracker.core.common.Telemetry
import com.eesuhn.habittracker.core.model.HabitId
import com.eesuhn.habittracker.core.ui.theme.AppTheme
import com.eesuhn.habittracker.feature.dashboard.ui.addhabit.AddHabitScreen
import com.eesuhn.habittracker.feature.dashboard.ui.dashboard.DashboardScreen
import com.eesuhn.habittracker.feature.dashboard.ui.habitdetail.HabitDetailScreen
import com.eesuhn.habittracker.feature.insights.ui.InsightsScreen
import com.eesuhn.habittracker.feature.misc.archive.ArchiveScreen
import com.eesuhn.habittracker.feature.misc.export.ExportScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint(ComponentActivity::class)
class MainActivity : Hilt_MainActivity() {

    @Inject
    lateinit var appPreferences: AppPreferences

    @Inject
    lateinit var navigationTelemetryLogger: NavigationTelemetryLogger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This app draws behind the system bars, so we want to handle fitting system windows
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val isDynamicColor by appPreferences.dynamicColorEnabled.collectAsState()
            AppTheme(isDynamicColor = isDynamicColor) {
                val navController = rememberNavController()
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = !isSystemInDarkTheme()
                val snackbarHostState = remember { SnackbarHostState() }

                DisposableEffect(systemUiController, useDarkIcons) {
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = useDarkIcons
                    )
                    onDispose { }
                }

                LaunchedEffect(navController) {
                    navController.addOnDestinationChangedListener(navigationTelemetryLogger)
                }

                Scaffold(
                    bottomBar = { AppNavigationBar(navController) },
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    modifier = Modifier.fillMaxSize(),
                    // Don't consume any insets. Even though a TopAppBar is not defined here, each
                    // screen adds one and handles the status bar inset, while giving each screen
                    // the chance to draw behind the status bar. The navigation bar inset is handled
                    // by the bottomBar too.
                    contentWindowInsets = WindowInsets(top = 0.dp)
                ) { innerPadding ->
                    Screens(navController, snackbarHostState, innerPadding)
                }
            }
        }
    }
}

@Composable
private fun Screens(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    padding: PaddingValues
) {
    val navigateBack: () -> Unit = { navController.popBackStack() }
    val navigateToSettings: () -> Unit = { navController.navigate(Destination.Settings) }
    val navigateToArchive: () -> Unit = { navController.navigate(Destination.Archive) }
    val navigateToExport: () -> Unit = { navController.navigate(Destination.Export) }
    val navigateToHabitDetails: (HabitId) -> Unit = { habitId ->
        val route = Destination.HabitDetails.buildRoute(habitId = habitId)
        navController.navigate(route)
    }

    NavHost(
        navController,
        startDestination = Destination.Dashboard.route,
        modifier = Modifier
            .padding(padding)
            .fillMaxSize()
    ) {
        appDestination(Destination.Dashboard) {
            DashboardScreen(
                hiltViewModel(),
                snackbarHostState,
                navigateToHabitDetails,
                navigateToAddHabit = { navController.navigate(Destination.AddHabit.route) },
                navigateToSettings,
                navigateToArchive,
                navigateToExport
            )
        }
        appDestination(Destination.Insights) {
            InsightsScreen(
                hiltViewModel(),
                navigateToArchive = navigateToArchive,
                navigateToSettings = navigateToSettings,
                navigateToExport = navigateToExport,
                navigateToHabitDetails = navigateToHabitDetails
            )
        }
        appDestination(Destination.AddHabit) { AddHabitScreen(hiltViewModel(), navigateBack) }
        appDestination(Destination.HabitDetails) { backStackEntry ->
            HabitDetailScreen(
                hiltViewModel(),
                habitId = Destination.HabitDetails.idFrom(backStackEntry.arguments),
                navigateBack
            )
        }
        appDestination(Destination.Archive) {
            ArchiveScreen(
                hiltViewModel(),
                snackbarHostState,
                navigateBack
            )
        }
        appDestination(Destination.Export) { ExportScreen(hiltViewModel(), navigateBack) }
    }
}

class NavigationTelemetryLogger @Inject constructor(
    private val telemetry: Telemetry
) : NavController.OnDestinationChangedListener {
    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        telemetry.leaveBreadcrumb(
            message = destination.route ?: "no-route",
            metadata = arguments?.let { mapOf("arguments" to it.toString()) } ?: emptyMap(),
            type = Telemetry.BreadcrumbType.Navigation
        )
    }
}
