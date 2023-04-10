package com.brd.weatheralerts.presentation

import com.brd.weatheralerts.domain.WeatherAlert
import com.brd.weatheralerts.presentation.screens.AlertDetailsUiState
import com.brd.weatheralerts.presentation.screens.ListAlertUiState
import java.text.SimpleDateFormat
import java.util.*

class AlertUiStateMapper {
    private val dateFormatter = SimpleDateFormat("HH:mm / MMM dd", Locale.getDefault())

    fun toAlertListItem(alert: WeatherAlert): ListAlertUiState {
        return ListAlertUiState(
            id = alert.id,
            name = alert.eventName,
            source = alert.source,
            startTime = dateFormatter.format(alert.startDate ?: Date()),
            endTime = dateFormatter.format(alert.endDate ?: Date())
        )
    }

    fun toAlertDetails(alert: WeatherAlert): AlertDetailsUiState {
        return AlertDetailsUiState(
                id = alert.id,
                    name = alert.eventName,
                    source = alert.source,
                    startTime = dateFormatter.format(alert.startDate ?: Date()),
                    endTime = dateFormatter.format(alert.endDate ?: Date()),
                    severity = alert.severity,
                    certainty = alert.certainty,
                    urgency = alert.urgency,
                    description = alert.description,
                    instructions = alert.instructions,
                    affectedZones = alert.affectedZones.map { Pair(it.name, it.isRadarStation) }
        )
    }
}