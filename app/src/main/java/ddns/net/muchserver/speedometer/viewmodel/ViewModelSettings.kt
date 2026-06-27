package ddns.net.muchserver.speedometer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.compose.ui.graphics.Color
import ddns.net.muchserver.speedometer.preferences.THEME_DARK
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ViewModelSettings: ViewModel() {
    private val _schemeColor = MutableStateFlow<List<Color>>(schemeLight(0))
    val schemeColor: StateFlow<List<Color>> = _schemeColor.asStateFlow()
    val setSchemeColor = { theme: String, indexTheme: Int ->
        if(theme == THEME_DARK) {
            _schemeColor.value = schemeDark(indexTheme)
        }
        else {
            _schemeColor.value = schemeLight(indexTheme)
        }
    }
    val isFullScreen: MutableLiveData<Boolean> = MutableLiveData(false)
    val setIsFullScreen = { isFull: Boolean -> isFullScreen.value = isFull }
    val isMenuExpanded: MutableLiveData<Boolean> = MutableLiveData(false)
    val setIsMenuExpanded = { isExpanded: Boolean -> isMenuExpanded.value = isExpanded}

    val isScreenAwake: MutableLiveData<Boolean> = MutableLiveData(false)
    val setIsScreenAwake = { awake: Boolean ->
        isScreenAwake.value = awake
    }

    val menuVisible: MutableLiveData<MenuVisible> = MutableLiveData(MenuVisible.MENU_MAP)
    val setMenuVisible = { menuEnum: MenuVisible -> menuVisible.value = menuEnum }
    val standardUnits: MutableLiveData<Boolean> = MutableLiveData(true)
    val setStandardUnits = {isStandard: Boolean -> standardUnits.value = isStandard}

    fun schemeDark(index: Int): List<Color> {
        when(index) {
            0 -> {
                return listOf(
                    Color(0xFF1D1E22),
                    Color(0xFFD4D4DC),
                    Color(0xFF393F4D),
                    Color(0xFFFEDA6A),
                    Color(0xFF393F4D),
                )
            }
            1 -> {
                return listOf(
                    Color(0xFF020312),
                    Color(0xFFFCFBF8),
                    Color(0xFF080D4B),
                    Color(0xFFB3081F),
                    Color(0xFF080D4B),
                )
            }
            2 -> {
                return listOf(
                    Color(0xFF000000),
                    Color(0xFFFFFFFF),
                    Color(0xFF0F1A38),
                    Color(0xFF5A6486),
                    Color(0xFF0F1A38),
                )
            }
            3 -> {
                return listOf(
                    Color(0xFF808080),
                    Color(0xFFFFFFFF),
                    Color(0xFF525E75),
                    Color(0xFFFF0054),
                    Color(0xFF525E75),
                )
            }
            else -> {
                return listOf(
                    Color(0xFF000000),
                    Color(0xFFFCFCFC),
                    Color(0xFFB8B6B5),
                    Color(0xFF8BC819),
                    Color(0xFFB8B6B5),
                )
            }
        }
    }

    fun schemeLight(index: Int): List<Color> {
        when(index) {
            0 -> {
                return listOf(
                    Color(0xFFE9F0F2),
                    Color(0xFF1C1C1C),
                    Color(0xFFFFFFFF),
                    Color(0xFF325A73),
                    Color(0xFFFFFFFF),
                )
            }
            1 -> {
                return listOf(
                    Color(0xFFFFFFFF), //Color(0xFF41ABFF),
                    Color(0xFF006CFF),
                    Color(0xFFEEEEEE),
                    Color(0xFF001835),
                    Color(0xFFFFFFFF),
                )
            }
            2 -> {
                return listOf(
                    Color(0xFFF9F8FD),
                    Color(0xFF3D3D3F),
                    Color(0xFFF6F5F3),
                    Color(0xFF7DCE94),
                    Color(0xFFF6F5F3),
                )
            }
            3 -> {
                return listOf(
                    Color(0xFFFFFFFF),
                    Color(0xFF003B5C),
                    Color(0xFFB0C4DE),
                    Color(0xFFFF6F20),
                    Color(0xFFB0C4DE),
                )
            }
            else -> {
                return listOf(
                    Color(0xFFFFFFFF),
                    Color(0xFF585858),
                    Color(0xFFD9D9D9),
                    Color(0xFFF1731C),
                    Color(0xFFD9D9D9),
                )
            }
        }
    }

}