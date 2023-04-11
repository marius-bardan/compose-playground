package com.brd.weatheralerts.data

import WeatherAlertsDto
import com.squareup.moshi.Moshi
import com.squareup.moshi.Rfc3339DateJsonAdapter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*

interface WeatherAlertsSource {
    suspend fun getAlerts(): Result<WeatherAlertsDto>
    fun isZoneRadar(zoneUrl: String): Boolean
}

object WeatherDataProvider : WeatherAlertsSource {
    private const val WEATHER_ALERTS_ENDPOINT = "https://api.weather.gov/"

    private val api = Retrofit.Builder()
        .baseUrl(WEATHER_ALERTS_ENDPOINT)
        .addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder()
                    .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
                    .build()
            )
        )
        .build()
        .create(WeatherApi::class.java)

    override suspend fun getAlerts() =
        try {
            val response = api.getAlerts()
            Result.success(response)
        } catch (t: Throwable) {
            Result.failure(t)
        }

    override fun isZoneRadar(zoneUrl: String): Boolean {
        return try {
            val zone = api.getZone(zoneUrl).execute().body()
            !zone?.properties?.radarStation.isNullOrEmpty()
        } catch (t: Throwable) {
            false
        }
    }
}