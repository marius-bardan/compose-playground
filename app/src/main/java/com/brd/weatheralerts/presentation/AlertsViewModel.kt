package com.brd.weatheralerts.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.brd.weatheralerts.R
import com.brd.weatheralerts.domain.WeatherAlert
import com.brd.weatheralerts.domain.WeatherAlertsRepository
import com.brd.weatheralerts.presentation.screens.AlertDetailsUiState
import com.brd.weatheralerts.presentation.screens.ListAlertUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class AlertsViewModel(
    private val source: WeatherAlertsRepository,
    private val mapper: AlertUiStateMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListItemsUiState(isLoading = true, items = listOf()))
    val uiState: StateFlow<ListItemsUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            val result = source.getAlerts()
            mapToUiState(result)
        }
    }

    private fun mapToUiState(result: Result<List<WeatherAlert>>): ListItemsUiState {
        val state = if (result.isFailure) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = ErrorMessage(R.string.generic_error)
                )
            }

            ListItemsUiState(errorMessage = ErrorMessage(R.string.generic_error))
        } else {
            val items = result.getOrDefault(emptyList())
            ListItemsUiState(
                items = items.map {
                    mapper.toAlertListItem(it)
                }
            )
        }

        _uiState.update { state }
        return state
    }

    fun onItemTap(alertId: String) {
        val alert = source.getAlertById(alertId)
        alert?.let {
            val selectedItem = mapper.toAlertDetails(alert)
            _uiState.update { it.copy(selectedItem = selectedItem) }
        }
    }

    fun onErrorDismiss() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }

    fun onDetailsBackAction() {
        _uiState.update {
            it.copy(selectedItem = null)
        }
    }

    companion object {
        fun provideFactory(
            source: WeatherAlertsRepository,
            mapper: AlertUiStateMapper
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AlertsViewModel(source, mapper) as T
                }
            }
    }
}

data class ListItemsUiState(
    val items: List<ListAlertUiState> = emptyList(),
    val selectedItem: AlertDetailsUiState? = null,
    val isLoading: Boolean = false,
    val errorMessage: ErrorMessage? = null
)