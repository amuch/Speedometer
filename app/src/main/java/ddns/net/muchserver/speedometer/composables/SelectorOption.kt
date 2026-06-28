package ddns.net.muchserver.speedometer.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ddns.net.muchserver.speedometer.MainActivity
import ddns.net.muchserver.speedometer.viewmodel.MenuVisible
import ddns.net.muchserver.speedometer.viewmodel.ViewModelPreferences
import ddns.net.muchserver.speedometer.viewmodel.ViewModelSettings
import ddns.net.muchserver.speedometer.viewmodel.ViewModelSpeedometer
import ddns.net.muchserver.speedometer.viewmodel.ViewModelTrip

@Composable
fun SelectorOption(
    modifier: Modifier,
    activity: MainActivity,
    colors: List<Color>,
    viewModelSettings: ViewModelSettings,
    viewModelPreferences: ViewModelPreferences,
    viewModelSpeedometer: ViewModelSpeedometer,
    viewModelTrip: ViewModelTrip
) {
    Box(
       modifier = modifier
    ) {
        val menuVisible by viewModelSettings.menuVisible.observeAsState(MenuVisible.MENU_MAP)
        val modifierOption = Modifier
            .fillMaxSize()
            .padding(PADDING_ROW)
            .border(2.dp, colors[INDEX_ACCENT], shape = RoundedCornerShape(10.dp))
        when(menuVisible) {
            MenuVisible.MENU_CONTROLS -> {
                LayoutSettings(
                    modifier = modifierOption,
                    activity = activity,
                    colors = colors,
                    viewModelPreferences = viewModelPreferences,
                    viewModelSettings = viewModelSettings
                )
            }
            MenuVisible.MENU_PREFERENCES -> {
                LayoutPreferences(
                    modifier = modifierOption,
                    activity = activity,
                    colors = colors,
                    viewModelPreferences = viewModelPreferences,
                    viewModelSettings = viewModelSettings
                )
            }
            MenuVisible.MENU_TRIP -> {
                LayoutTrip(
                    modifier = modifierOption,
                    colors = colors,
                    viewModelSettings = viewModelSettings,
                    viewModelSpeedometer = viewModelSpeedometer,
                    viewModelTrip = viewModelTrip
                )
            }
            MenuVisible.MENU_MAP,
            null -> {
                MapGauge(
                    modifier = modifierOption,
                    viewModelPreferences = viewModelPreferences,
                    viewModelSpeedometer = viewModelSpeedometer
                )
            }
        }
    }
}