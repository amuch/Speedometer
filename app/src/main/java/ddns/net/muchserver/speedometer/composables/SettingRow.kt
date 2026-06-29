package ddns.net.muchserver.speedometer.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ddns.net.muchserver.speedometer.viewmodel.INDEX_SETTINGS_NONE
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
    val indexOpen: Int by viewModelSettings.indexOpenSetting.observeAsState(0)
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
            Button(
                modifier = Modifier
                    .padding(10.dp),
                onClick = {
                    if(indexOpen == index) {
                        viewModelSettings.setIndexOpenSetting(INDEX_SETTINGS_NONE)
                    }
                    else {
                        viewModelSettings.setIndexOpenSetting(index)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors[INDEX_BACKGROUND_BUTTON],
                    contentColor = colors[INDEX_TEXT_BUTTON]
                )
            ) {
                Text(text= "?")
            }
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
        AnimatedVisibility(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.Transparent
                ),
            enter = expandVertically(animationSpec = tween(durationMillis = MILLISECONDS_ANIMATE_IN)),
            exit = shrinkVertically(animationSpec = tween(durationMillis = MILLISECONDS_ANIMATE_OUT)),
            visible = indexOpen == index
        ) {
            Text(
                modifier = Modifier.padding(PADDING_ROW),
                text = messages[INDEX_MESSAGE],
                color = colors[INDEX_TEXT]
            )
        }
    }
}