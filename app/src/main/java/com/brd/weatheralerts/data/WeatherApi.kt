package com.brd.weatheralerts.data

import WeatherAlertsDto
import ZoneDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Url

interface WeatherApi {
  @Headers("Accept: application/geo+json")
  @GET("/alerts/active?status=actual&message_type=alert")
  suspend fun getAlerts(): WeatherAlertsDto

  @Headers("Accept: application/geo+json")
  @GET
  fun getZone(@Url url: String): Call<ZoneDto>

}
