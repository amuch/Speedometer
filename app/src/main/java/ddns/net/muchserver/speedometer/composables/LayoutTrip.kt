package ddns.net.muchserver.speedometer.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun LayoutTrip(
    modifier: Modifier,
    colors: List<Color>,
) {
    Column(
        modifier = modifier
    ) {
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = colors[INDEX_BACKGROUND_BUTTON],
                contentColor = colors[INDEX_TEXT_BUTTON]
            )
        ) {
            Text(
                text = "Trip"
            )
        }
    }
}