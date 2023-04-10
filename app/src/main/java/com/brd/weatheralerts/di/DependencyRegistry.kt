package com.brd.weatheralerts.di

import com.brd.weatheralerts.data.WeatherDataProvider
import com.brd.weatheralerts.data.WeatherAlertsSource
import com.brd.weatheralerts.domain.WeatherAlertsRepository
import com.brd.weatheralerts.presentation.AlertUiStateMapper

class DependencyRegistry {
    private val dataSource: WeatherAlertsSource by lazy {
        WeatherDataProvider
    }

    val repository: WeatherAlertsRepository by lazy {
        WeatherAlertsRepository(source = dataSource)
    }

    val itemUiStateMapper: AlertUiStateMapper by lazy {
        AlertUiStateMapper()
    }
}