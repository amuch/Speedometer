package ddns.net.muchserver.speedometer.composables

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import ddns.net.muchserver.speedometer.MainActivity
import ddns.net.muchserver.speedometer.service.ServiceForeground
import ddns.net.muchserver.speedometer.viewmodel.ViewModelPreferences
import ddns.net.muchserver.speedometer.viewmodel.ViewModelSettings

@Composable
fun LayoutSettings(
    modifier: Modifier,
    activity: MainActivity,
    colors: List<Color>,
    viewModelPreferences: ViewModelPreferences,
    viewModelSettings: ViewModelSettings
) {
    val scrollState = rememberScrollState()
    var isScreenAwake by remember { mutableStateOf(false) }
    viewModelSettings.isScreenAwake.observe(activity) {
        isScreenAwake = it
    }

    Column(
        modifier = modifier.then(
            Modifier
                .fillMaxSize()
                .background(
                    color = Color.Transparent
                )
                .verticalScroll(scrollState)
        )
    ) {
        val context = LocalContext.current
        val isServiceRunning = ServiceForeground.isServiceRunning
        SettingRow(
            modifier = Modifier.fillMaxWidth(),
            colors = colors,
            viewModelSettings = viewModelSettings,
            isSet = isServiceRunning,
            setCheckedOn = {
                Intent(context, ServiceForeground::class.java).also {
                    it.action = ServiceForeground.Actions.START.toString()
                    context.startService(it)
                }
            },
            setCheckedOff = {
                Intent(context, ServiceForeground::class.java).also {
                    it.action = ServiceForeground.Actions.STOP.toString()
                    context.startService(it)
                }
            },
            messages = arrayOf(
                "Service On",
                "Service Off",
                "Enable to run a background service. Location updates can continue in the background if enabled."
            ),
            index = 2,
            composable = null
        )
        SettingRow(
            modifier = Modifier.fillMaxWidth(),
            colors = colors,
            viewModelSettings = viewModelSettings,
            isSet = isScreenAwake,
            setCheckedOn = { viewModelSettings.setIsScreenAwake(true) },
            setCheckedOff = {viewModelSettings.setIsScreenAwake(false) },
            messages = arrayOf(
                "Screen On",
                "Screen Off",
                "Enable to ensure that screen stays illuminated while the app is running. This will reduce battery life."
            ),
            index = 3,
            composable = null
        )
    }
}