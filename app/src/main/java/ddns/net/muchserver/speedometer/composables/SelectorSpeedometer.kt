package ddns.net.muchserver.speedometer.composables

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ddns.net.muchserver.speedometer.R
import ddns.net.muchserver.speedometer.viewmodel.MenuVisible
import ddns.net.muchserver.speedometer.viewmodel.ViewModelSettings
import ddns.net.muchserver.speedometer.viewmodel.ViewModelSpeedometer

const val MILLISECONDS_ANIMATE_IN = 800

const val MILLISECONDS_SHRINK_HORIZONTALLY = 200
const val MILLISECONDS_SHRINK_VERTICALLY = 500


fun rotationIconFullScreen(orientation: Int, isFullScreen: Boolean): Float {
    if(!isFullScreen) {
        return 0f
    }
    if(orientation == Configuration.ORIENTATION_PORTRAIT) {
        return 270f
    }

    return 180f
}

@Composable
fun SelectorSpeedometer(
    modifier: Modifier,
    colors: List<Color>,
    isFullScreen: Boolean,
    setIsFullScreen: (Boolean) -> Unit,
    isMenuExpanded: Boolean,
    setIsMenuExpanded: (Boolean) -> Unit,
    orientation: Int,
    viewModelSettings: ViewModelSettings,
    viewModelSpeedometer: ViewModelSpeedometer,
) {
    val scrollStateMenu = rememberScrollState()
    val rotationIconFull = rotationIconFullScreen(orientation, isFullScreen)

    Box(
        modifier = modifier.then(Modifier.background(Color.Transparent))
    ) {
        SpeedometerFull(
            modifier = Modifier.fillMaxSize(),
            colors = colors,
            viewModelSpeedometer = viewModelSpeedometer,
            viewModelSettings = viewModelSettings
        )
        val modifierButton = Modifier.width(60.dp)
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.BottomEnd)
                .padding(5.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            AnimatedVisibility(
                modifier = Modifier.verticalScroll(scrollStateMenu),
                enter = expandVertically(animationSpec = tween(durationMillis = MILLISECONDS_ANIMATE_IN)),
                exit = shrinkVertically(animationSpec = tween(durationMillis = MILLISECONDS_SHRINK_VERTICALLY)),
                visible = isMenuExpanded
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight().align(Alignment.CenterHorizontally)
                ) {
                    if(!isFullScreen) {
                        Button(
                            modifier = modifierButton,
                            onClick = { viewModelSettings.setMenuVisible(MenuVisible.MENU_MAP) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colors[INDEX_BACKGROUND_BUTTON],
                                contentColor = colors[INDEX_TEXT_BUTTON]
                            )
                        ) {
                            Icon(
                                modifier = Modifier.rotate(rotationIconFull),
                                painter = painterResource(id = R.drawable.map_point_48),
                                contentDescription = "Preferences",
                                tint = colors[INDEX_TEXT_BUTTON]
                            )
                        }
                        Button(
                            modifier = modifierButton,
                            onClick = { viewModelSettings.setMenuVisible(MenuVisible.MENU_TRIP) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colors[INDEX_BACKGROUND_BUTTON],
                                contentColor = colors[INDEX_TEXT_BUTTON]
                            )
                        ) {
                            Icon(
                                modifier = Modifier.rotate(rotationIconFull),
                                painter = painterResource(id = R.drawable.journey_24),
                                contentDescription = "Trip",
                                tint = colors[INDEX_TEXT_BUTTON]
                            )
                        }
                        Button(
                            modifier = modifierButton,
                            onClick = { viewModelSettings.setMenuVisible(MenuVisible.MENU_CONTROLS) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colors[INDEX_BACKGROUND_BUTTON],
                                contentColor = colors[INDEX_TEXT_BUTTON]
                            )
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.settings_sliders_24),
                                contentDescription = "controls",
                                tint = colors[INDEX_TEXT_BUTTON]
                            )
                        }
                        Button(
                            modifier = modifierButton,
                            onClick = { viewModelSettings.setMenuVisible(MenuVisible.MENU_PREFERENCES) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colors[INDEX_BACKGROUND_BUTTON],
                                contentColor = colors[INDEX_TEXT_BUTTON]
                            )
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.settings_24),
                                contentDescription = "Preferences",
                                tint = colors[INDEX_TEXT_BUTTON]
                            )
                        }
                    }
                    Button(
                        modifier = modifierButton,
                        onClick = { viewModelSpeedometer.resetSpeedMax() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors[INDEX_BACKGROUND_BUTTON],
                            contentColor = colors[INDEX_TEXT_BUTTON]
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.refresh_24),
                            contentDescription = "Reset",
                            tint = colors[INDEX_TEXT_BUTTON]
                        )
                    }
                    Button(
                        modifier = modifierButton,
                        onClick = {
                            if(!isFullScreen) {
                                setIsMenuExpanded(false)
                                viewModelSettings.setMenuVisible(MenuVisible.MENU_MAP)
                            }
                            setIsFullScreen(!isFullScreen)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors[INDEX_BACKGROUND_BUTTON],
                            contentColor = colors[INDEX_TEXT_BUTTON]
                        )
                    ) {
                        val id = if(isFullScreen) R.drawable.right_24 else R.drawable.expand_24
                        Icon(
                            modifier = Modifier.rotate(rotationIconFull),
                            painter = painterResource(id = id),
                            contentDescription = "Full Screen",
                            tint = colors[INDEX_TEXT_BUTTON]
                        )
                    }
                }
            }

            Button(
                modifier = modifierButton,
                onClick = { setIsMenuExpanded(!isMenuExpanded) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors[INDEX_BACKGROUND_BUTTON],
                    contentColor = colors[INDEX_TEXT_BUTTON]
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.menu_dots_vertical_24),
                    contentDescription = "Menu",
                    tint = colors[INDEX_TEXT_BUTTON]
                )
            }
        }
    }
}