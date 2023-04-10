package com.brd.weatheralerts.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.brd.weatheralerts.di.DependencyRegistry
import com.brd.weatheralerts.presentation.nav.AlertsNavGraph
import com.brd.weatheralerts.presentation.theme.WeatherAlertsTheme

@Composable
fun WeatherAlertsApp(
    registry: DependencyRegistry
) {
    WeatherAlertsTheme {
        val navController = rememberNavController()
        AlertsNavGraph(
            registry = registry,
            navController = navController
        )
    }
}