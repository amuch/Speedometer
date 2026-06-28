package ddns.net.muchserver.speedometer.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

@Composable
fun DisplayRow(
    modifier: Modifier,
    colors: List<Color>,
    label: String,
    text: String
) {
    Text(
        modifier = modifier,
        text = label,
        textAlign = TextAlign.Center,
        color = colors[INDEX_TEXT]
    )
    Text(
        modifier = modifier,
        text = text,
        textAlign = TextAlign.Center,
        color = colors[INDEX_TEXT]
    )
}