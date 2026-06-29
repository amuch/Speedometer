package ddns.net.muchserver.speedometer.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ddns.net.muchserver.speedometer.database.Trip
import ddns.net.muchserver.speedometer.viewmodel.ViewModelTrip

val FONT_SIZE_TRIP = 20.sp

@Composable
fun TripRow(
    modifier: Modifier,
    colors: List<Color>,
    viewModelTrip: ViewModelTrip,
    trip: Trip
) {
    var isExpanded by remember { mutableStateOf(false) }
    val fractionHeight = if(isExpanded) 0.5f else 1.0f
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fractionHeight)
                .padding(horizontal = PADDING_ROW)
                .clickable(enabled = true) {
                isExpanded = !isExpanded
            }
        ) {
            Text(
                text = trip.name,
                color = colors[INDEX_TEXT],
                fontSize = FONT_SIZE_TRIP
            )
        }
        if(isExpanded) {
            Row(
                modifier = modifier
                    .fillMaxSize()
                    .padding(PADDING_ROW)
            ) {
                Button(
                    onClick = {
                        viewModelTrip.deleteTrip(trip.id)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors[INDEX_BACKGROUND_BUTTON],
                        contentColor = colors[INDEX_TEXT_BUTTON]
                    )
                )  {
                    Text(text = "Delete")
                }
            }
        }
    }
}