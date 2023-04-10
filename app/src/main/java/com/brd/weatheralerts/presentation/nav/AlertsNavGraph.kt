package com.brd.weatheralerts.presentation.nav

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.brd.weatheralerts.di.DependencyRegistry
import com.brd.weatheralerts.presentation.AlertsViewModel
import com.brd.weatheralerts.presentation.list.AppRoute

@Composable
fun AlertsNavGraph(
    registry: DependencyRegistry,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavDestinations.HOME_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(NavDestinations.HOME_ROUTE) {
            val alertsViewModel: AlertsViewModel = viewModel(
                factory = AlertsViewModel.provideFactory(
                    source = registry.repository,
                    mapper = registry.itemUiStateMapper
                )
            )
            val lazyListState = rememberLazyListState()
            AppRoute(
                viewModel = alertsViewModel,
                lazyListState = lazyListState,
                onDetailsBackPressed = { alertsViewModel.onDetailsBackAction() }
            )
        }
    }
}
