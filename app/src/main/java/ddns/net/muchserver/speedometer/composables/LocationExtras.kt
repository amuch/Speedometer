package ddns.net.muchserver.speedometer.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ddns.net.muchserver.speedometer.viewmodel.ViewModelSpeedometer
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ddns.net.muchserver.speedometer.logic.DECIMAL_FORMAT_TENTHS
import ddns.net.muchserver.speedometer.logic.calculateDirection
import ddns.net.muchserver.speedometer.logic.formatAltitude
import ddns.net.muchserver.speedometer.logic.formatLatitude
import ddns.net.muchserver.speedometer.logic.formatLongitude
import ddns.net.muchserver.speedometer.viewmodel.ViewModelSettings

@Composable
fun LocationExtras(
    modifier: Modifier,
    colors: List<Color>,
    viewModelSpeedometer: ViewModelSpeedometer,
    viewModelSettings: ViewModelSettings
) {
    val location by viewModelSpeedometer.currentLocation.collectAsStateWithLifecycle()
    val isStandardUnits: Boolean by viewModelSettings.standardUnits.observeAsState(true)
    Column(
        modifier = modifier.then(
            Modifier
                    .background(Color.Transparent)
                    .border(
                        width = 2.dp,
                        color = colors[INDEX_ACCENT],
                        shape = RoundedCornerShape(10.dp)
                    ),
        )
    ) {
        val altitude = formatAltitude(location?.altitude ?: 0.0, isStandardUnits)
        val latitude = formatLatitude(location?.latitude ?: 0.0)
        val longitude = formatLongitude(location?.longitude ?: 0.0)
        val bearing = location?.bearing ?: 0.0
        val bearingDegrees = DECIMAL_FORMAT_TENTHS.format(bearing).toString()
        val bearingDirection = calculateDirection(bearing.toFloat())

        val modifier = Modifier.fillMaxWidth().padding(2.dp)
        DisplayRow(
            modifier = modifier,
            colors = colors,
            label = "Altitude",
            text = altitude
        )
        DisplayRow(
            modifier = modifier,
            colors = colors,
            label = "Bearing",
            text = "$bearingDegrees ($bearingDirection)"
        )
        DisplayRow(
            modifier = modifier,
            colors = colors,
            label = "Latitude",
            text = latitude
        )
        DisplayRow(
            modifier = modifier,
            colors = colors,
            label = "Longitude",
            text = longitude
        )
    }
}