package ddns.net.muchserver.speedometer.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import ddns.net.muchserver.speedometer.viewmodel.ViewModelPreferences
import ddns.net.muchserver.speedometer.viewmodel.ViewModelSpeedometer
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import ddns.net.muchserver.speedometer.logic.formatLatitude
import ddns.net.muchserver.speedometer.logic.formatLongitude


const val ZOOM_DEFAULT = 11.0f
const val DISPLACEMENT_CAMERA_POSITION_MAX = 2

const val HUE_RED = 0.0f

@Composable
fun MapGauge(
    modifier: Modifier,
    viewModelPreferences: ViewModelPreferences,
    viewModelSpeedometer: ViewModelSpeedometer
) {
    val location by viewModelSpeedometer.currentLocation.collectAsStateWithLifecycle()

    var latLngCurrent by remember { mutableStateOf(LatLng(0.0, 0.0) ) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLngCurrent, ZOOM_DEFAULT)
    }

    LaunchedEffect(location) {
        location?.let {
            val updatedLatLng = LatLng(it.latitude, it.longitude)
            latLngCurrent = updatedLatLng
            val speed = it.speed
            val altitude = it.altitude

//            cameraPositionState.animate(
//                update = CameraUpdateFactory.newLatLngZoom(updatedLatLng, cameraPositionState.position.zoom)
//            )
        }
    }

    if(cameraPositionState.position.target.latitude - latLngCurrent.latitude > DISPLACEMENT_CAMERA_POSITION_MAX ||
        cameraPositionState.position.target.longitude - latLngCurrent.longitude > DISPLACEMENT_CAMERA_POSITION_MAX
    ) {
        val zoom = cameraPositionState.position.zoom
        cameraPositionState.position = CameraPosition.fromLatLngZoom(latLngCurrent, zoom)
    }

    GoogleMap(
        modifier = modifier.then(
            Modifier
                .fillMaxSize()
                .padding(5.dp)
        ),
        cameraPositionState = cameraPositionState,
//        properties = MapProperties(mapType = MapType.NORMAL)
    ) {
        Marker(
            position = latLngCurrent,
            title = "${formatLatitude(latLngCurrent.latitude)}, ${formatLongitude(latLngCurrent.longitude)}",
            snippet = "Current Location",
            alpha = 0.8f,
            icon = BitmapDescriptorFactory.defaultMarker(HUE_RED)
        )
    }
}