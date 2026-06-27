package ddns.net.muchserver.speedometer.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ddns.net.muchserver.speedometer.MainActivity
import ddns.net.muchserver.speedometer.preferences.KEY_INDEX_THEME
import ddns.net.muchserver.speedometer.preferences.KEY_STANDARD_UNITS
import ddns.net.muchserver.speedometer.preferences.KEY_THEME
import ddns.net.muchserver.speedometer.preferences.THEME_DARK
import ddns.net.muchserver.speedometer.preferences.THEME_LIGHT
import ddns.net.muchserver.speedometer.viewmodel.ViewModelPreferences
import ddns.net.muchserver.speedometer.viewmodel.ViewModelSettings

@Composable
fun LayoutPreferences(
    modifier: Modifier,
    activity: MainActivity,
    colors: List<Color>,
    viewModelPreferences: ViewModelPreferences,
    viewModelSettings: ViewModelSettings
) {
    var indexTheme by remember { mutableIntStateOf(0) }
    var theme by remember { mutableStateOf(THEME_LIGHT)}
    var standardUnits by remember { mutableStateOf(true) }
    viewModelPreferences.readFromDataStore.observe(activity) { preferences ->
        theme = preferences.theme
        indexTheme = preferences.indexTheme
        standardUnits = preferences.standardUnits
    }
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.then(
            Modifier.fillMaxSize()
                .background(color = Color.Transparent)
                .verticalScroll(scrollState)
        )
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.12f))
        SettingRow(
            modifier = Modifier
                .fillMaxWidth(),
            colors = colors,
            viewModelSettings = viewModelSettings,
            isSet = theme == THEME_DARK,
            setCheckedOn = {
                viewModelSettings.setSchemeColor(THEME_DARK, indexTheme)
                viewModelPreferences.saveToDataStore(KEY_THEME, THEME_DARK)
            },
            setCheckedOff = {
                viewModelSettings.setSchemeColor(THEME_LIGHT, indexTheme)
                viewModelPreferences.saveToDataStore(KEY_THEME, THEME_LIGHT)
            },
            messages = arrayOf(
                "Dark Mode",
                "Light Mode",
                "This setting is responsible for setting the color scheme of the application."
            ),
            index = 0,
            composable = {
                var sliderPosition by remember { mutableFloatStateOf(indexTheme.toFloat()) }
                Slider(
                    modifier = Modifier.padding(20.dp),
                    value = sliderPosition,
                    colors = SliderDefaults.colors(
                        thumbColor = colors[INDEX_BACKGROUND_BUTTON],
                        activeTrackColor = colors[INDEX_TEXT_BUTTON],
                        inactiveTrackColor = colors[INDEX_TEXT_BUTTON]
                    ),
                    onValueChange = {
                        sliderPosition = it
                        val sliderPositionInt = it.toInt()
                        if(sliderPositionInt != indexTheme) {
                            indexTheme = sliderPositionInt
                            viewModelSettings.setSchemeColor(theme, sliderPositionInt)
                            viewModelPreferences.saveToDataStore(KEY_INDEX_THEME, sliderPositionInt)
                        }
                    },
                    steps = 3,
                    valueRange = 0f .. 4f
                )
            }
        )
        SettingRow(
            modifier = Modifier
                .fillMaxWidth(),
            colors = colors,
            viewModelSettings = viewModelSettings,
            isSet = standardUnits,
            setCheckedOn = { viewModelPreferences.saveToDataStore(KEY_STANDARD_UNITS, true) },
            setCheckedOff = { viewModelPreferences.saveToDataStore(KEY_STANDARD_UNITS, false) },
            messages = arrayOf(
                "Standard Units",
                "SI Units",
                "This setting determines the units in which the speed, distance, and altitude are reported. When toggled, speed will be reported in miles per hour, distance in miles, and altitude in feet. Otherwise, speed will be reported in kilometers per hour, distance in kilometers, and altitude in meters."
            ),
            index = 1,
        )
    }
}