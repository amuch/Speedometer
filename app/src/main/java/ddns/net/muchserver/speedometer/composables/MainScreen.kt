package ddns.net.muchserver.speedometer.composables

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ddns.net.muchserver.speedometer.MainActivity
import ddns.net.muchserver.speedometer.viewmodel.ViewModelPreferences
import ddns.net.muchserver.speedometer.viewmodel.ViewModelSettings
import ddns.net.muchserver.speedometer.viewmodel.ViewModelSpeedometer

const val INDEX_BACKGROUND = 0
const val INDEX_TEXT = 1
const val INDEX_ACCENT = 2
const val INDEX_BACKGROUND_BUTTON = 3
const val INDEX_TEXT_BUTTON = 4

@Composable
fun MainScreen(
    modifier: Modifier,
    activity: MainActivity,
    viewModelSettings: ViewModelSettings,
    viewModelPreferences: ViewModelPreferences,
    viewModelSpeedometer: ViewModelSpeedometer
) {
    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation

    val colors by viewModelSettings.schemeColor.collectAsStateWithLifecycle()

    val isFull = viewModelSettings.isFullScreen.value ?: false
    var isFullScreen by remember { mutableStateOf(isFull) }
    if(isFullScreen) {
        LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
    }

    val isExpanded = viewModelSettings.isMenuExpanded.value ?: false
    var isMenuExpanded by remember { mutableStateOf(isExpanded) }


    val weightSpeedometer by animateFloatAsState(
        targetValue = if(isFullScreen) 1f else 0.5f,
        label = "weightSpeedometer"
    )
    val weightMap by animateFloatAsState(
        targetValue = if(isFullScreen) 0f else 0.5f,
        label = "weightMap"
    )

    val modifierSpeedometer =
        if(Configuration.ORIENTATION_PORTRAIT == orientation)
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(weightSpeedometer)
        else Modifier
            .fillMaxWidth(weightSpeedometer)
            .fillMaxHeight()

    val modifierOption =
        if(Configuration.ORIENTATION_PORTRAIT == orientation)
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(weightMap)
        else Modifier
            .fillMaxWidth(weightMap)
            .fillMaxHeight()

    Box(
        modifier = modifier.then(
            Modifier
            .fillMaxSize()
            .background(color = colors[INDEX_BACKGROUND])
        )
    ) {
        SelectorSpeedometer(
            modifier = modifierSpeedometer.then(
                Modifier
                    .align(
                        if(Configuration.ORIENTATION_PORTRAIT == orientation) Alignment.TopCenter
                        else Alignment.CenterStart
                    )
                    .padding(10.dp)
                    .background(color = Color.Transparent)
            ),
            colors = colors,
            isFullScreen = isFullScreen,
            setIsFullScreen = {
                isFullScreen = it
                viewModelSettings.setIsFullScreen(it)
            },
            isMenuExpanded = isMenuExpanded,
            setIsMenuExpanded = {
                isMenuExpanded = it
                viewModelSettings.setIsMenuExpanded(it)
            },
            orientation = orientation,
            viewModelSettings = viewModelSettings,
            viewModelSpeedometer = viewModelSpeedometer
        )

        SelectorOption(
            modifier = modifierOption.then(
                Modifier
                    .align(
                        if(Configuration.ORIENTATION_PORTRAIT == orientation) Alignment.BottomCenter
                        else Alignment.CenterEnd
                    )
                    .background(color = Color.Transparent)
            ),
            activity = activity,
            colors = colors,
            viewModelPreferences = viewModelPreferences,
            viewModelSpeedometer =  viewModelSpeedometer,
            viewModelSettings = viewModelSettings
        )
    }
}

@Composable
fun LockScreenOrientation(orientation: Int) {
    val activity = LocalActivity.current
    DisposableEffect(orientation) {
        val activity = activity ?: return@DisposableEffect onDispose {}
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation
        onDispose {
            activity.requestedOrientation = originalOrientation
        }
    }
}