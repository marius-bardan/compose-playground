package com.brd.weatheralerts.domain

import java.util.Date

data class WeatherAlert(
    val id: String,
    val eventName: String,
    val startDate: Date?,
    val endDate: Date?,
    val source: String,
    val severity: String,
    val certainty: String,
    val urgency: String,
    val description: String?,
    val affectedZones: List<WeatherAffectedZone>,
    val instructions: String?
)

data class WeatherAffectedZone(
    val name: String,
    var isRadarStation: Boolean? = null
)