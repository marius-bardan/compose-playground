package com.brd.weatheralerts.presentation.list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brd.weatheralerts.presentation.AlertsViewModel
import com.brd.weatheralerts.presentation.ListItemsUiState
import com.brd.weatheralerts.presentation.screens.AlertDetails
import com.brd.weatheralerts.presentation.screens.AlertDetailsUiState
import com.brd.weatheralerts.presentation.screens.AlertsList

@Composable
fun AppRoute(
    viewModel: AlertsViewModel,
    lazyListState: LazyListState,
    onDetailsBackPressed: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.selectedItem == null) {
        ListRoute(listItemsUiState = uiState,
            lazyListState = lazyListState,
            tapHandler = { viewModel.onItemTap(it) },
            onErrorDismiss = { viewModel.onErrorDismiss() })
    } else {
        DatailsRoute(alertDetailsUiState = uiState.selectedItem!!)

        BackHandler {
            onDetailsBackPressed()
        }
    }

}

@Composable
fun ListRoute(
    listItemsUiState: ListItemsUiState,
    lazyListState: LazyListState,
    tapHandler: (String) -> Unit,
    onErrorDismiss: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    AlertsList(
        uiState = listItemsUiState,
        tapHandler = tapHandler,
        onErrorDismiss = onErrorDismiss,
        snackbarHostState = snackbarHostState,
        itemsLazyListState = lazyListState
    )
}

@Composable
fun DatailsRoute(alertDetailsUiState: AlertDetailsUiState) {
    AlertDetails(state = alertDetailsUiState)
}