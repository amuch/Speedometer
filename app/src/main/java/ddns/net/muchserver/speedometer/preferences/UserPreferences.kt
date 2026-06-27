package ddns.net.muchserver.speedometer.preferences

data class UserPreferences(
    val theme: String,
    val indexTheme: Int,
    val standardUnits: Boolean,
    val updateInterval: Long,
)

