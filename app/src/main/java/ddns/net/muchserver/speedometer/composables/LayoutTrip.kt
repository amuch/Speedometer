package ddns.net.muchserver.speedometer.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ddns.net.muchserver.speedometer.viewmodel.ViewModelSettings
import ddns.net.muchserver.speedometer.viewmodel.ViewModelSpeedometer
import ddns.net.muchserver.speedometer.viewmodel.ViewModelTrip

@Composable
fun LayoutTrip(
    modifier: Modifier,
    colors: List<Color>,
    viewModelSettings: ViewModelSettings,
    viewModelSpeedometer: ViewModelSpeedometer,
    viewModelTrip: ViewModelTrip
) {
    Column(
        modifier = modifier
    ) {
        val scrollState = rememberScrollState()
        val trips by viewModelTrip.trips.observeAsState(listOf())

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .verticalScroll(scrollState)
        ) {
            val modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.15f)
            for(trip in trips) {
                TripRow(
                    modifier = modifier,
                    colors = colors,
                    viewModelTrip = viewModelTrip,
                    trip = trip
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Button(
                onClick = {
                    viewModelTrip.startTrip()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors[INDEX_BACKGROUND_BUTTON],
                    contentColor = colors[INDEX_TEXT_BUTTON]
                )
            ) {
                Text(
                    text = "Start"
                )
            }
            Button(
                onClick = {
                    viewModelTrip.stopTrip()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors[INDEX_BACKGROUND_BUTTON],
                    contentColor = colors[INDEX_TEXT_BUTTON]
                )
            ) {
                Text(
                    text = "Stop"
                )
            }
        }
    }
}