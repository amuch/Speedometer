package ddns.net.muchserver.speedometer.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ddns.net.muchserver.speedometer.viewmodel.ViewModelSettings

const val INDEX_ACTIVE = 0
const val INDEX_INACTIVE = 1
const val INDEX_MESSAGE = 2

@Composable
fun SettingRow(
    modifier: Modifier,
    colors: List<Color>,
    viewModelSettings: ViewModelSettings,
    isSet: Boolean,
    setCheckedOn: () -> Unit,
    setCheckedOff: () -> Unit,
    messages: Array<String>,
    index: Int,
    composable: @Composable (() -> Unit)? = null
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(
                modifier = Modifier
                    .padding(10.dp),
                checked = isSet,
                onCheckedChange = {
                    if(isSet) setCheckedOff() else setCheckedOn()
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colors[INDEX_BACKGROUND_BUTTON],
                    checkedTrackColor = colors[INDEX_BACKGROUND],
                    checkedBorderColor = colors[INDEX_TEXT_BUTTON],
                    uncheckedThumbColor = colors[INDEX_BACKGROUND_BUTTON],
                    uncheckedTrackColor = colors[INDEX_BACKGROUND],
                    uncheckedBorderColor = colors[INDEX_TEXT_BUTTON]
                )
            )
            Text(
                modifier = Modifier.padding(10.dp),
                text = if(isSet) messages[INDEX_ACTIVE] else messages[INDEX_INACTIVE],
                color = colors[INDEX_TEXT]
            )
        }
        if(composable != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                composable()
            }
        }
    }
}