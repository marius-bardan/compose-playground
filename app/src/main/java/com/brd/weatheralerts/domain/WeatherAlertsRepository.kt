package com.brd.weatheralerts.domain

import WeatherAlertsDto
import com.brd.weatheralerts.data.WeatherAlertsSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

class WeatherAlertsRepository(private val source: WeatherAlertsSource) {
    private val cache = mutableListOf<WeatherAlert>()
    private val resolvedZones = mutableMapOf<String, Boolean?>()

    suspend fun getAlerts(): Result<List<WeatherAlert>> {
        val response = source.getAlerts()
        val result: Result<List<WeatherAlert>> = if (response.isFailure) {
            cache.clear()
            Result.failure(response.exceptionOrNull()!!)
        } else {
            val mapped = mapToModel(response.getOrNull()!!)
            mapped
                .forEach { alert ->
                    cache.add(alert)
                }
            Result.success(mapped)
        }
        return result
    }

    fun getAlertById(id: String): WeatherAlert? {
        return cache.find { it.id == id }
    }

    private fun mapToModel(value: WeatherAlertsDto): List<WeatherAlert> =
        value.features.map { feature ->
            WeatherAlert(
                id = feature.properties.id,
                eventName = feature.properties.event,
                startDate = feature.properties.onset,
                endDate = feature.properties.expires,
                source = feature.properties.senderName,
                severity = feature.properties.severity,
                certainty = feature.properties.certainty,
                urgency = feature.properties.urgency,
                description = feature.properties.description,
                affectedZones = feature.properties.affectedZones.map {
                    WeatherAffectedZone(
                        it,
                        false
                    )
                },
                instructions = feature.properties.instruction
            ).also {
                mapZoneRadar(it)
            }
        }

    private fun mapZoneRadar(alert: WeatherAlert) {
        //best effort
        alert.affectedZones.forEach { zone ->
            if (resolvedZones.containsKey(zone.name)) {
                zone.isRadarStation = resolvedZones[zone.name]
            } else {
                resolvedZones[zone.name] = null
                Dispatchers.IO.asExecutor().execute {
                    zone.isRadarStation = source.isZoneRadar(alert.affectedZones.first().name)
                    resolvedZones[zone.name] = zone.isRadarStation
                }
            }
        }
    }
}