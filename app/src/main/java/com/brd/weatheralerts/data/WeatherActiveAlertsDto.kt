import com.squareup.moshi.Json
import java.util.Date

data class WeatherAlertsDto(
    @Json(name = "features") val features: List<Feature>
)

data class Feature(
    @Json(name = "id") val id: String,
    @Json(name = "type") val type: String,
    @Json(name = "properties") val properties: AlertProperties
)

data class AlertProperties(
    @Json(name = "@id") val id: String,
    @Json(name = "id") val propertyId: String,
    @Json(name = "affectedZones") val affectedZones: List<String>,
    @Json(name = "effective") val effective: String,
    @Json(name = "onset") val onset: Date?,
    @Json(name = "expires") val expires: Date?,
    @Json(name = "severity") val severity: String,
    @Json(name = "certainty") val certainty: String,
    @Json(name = "urgency") val urgency: String,
    @Json(name = "event") val event: String,
    @Json(name = "senderName") val senderName: String,
    @Json(name = "headline") val headline: String,
    @Json(name = "description") val description: String,
    @Json(name = "instruction") val instruction: String?
)

data class ZoneDto(
    @Json(name = "properties") val properties: ZoneProperties
)

data class ZoneProperties(
    @Json(name = "radarStation") val radarStation: String?
)