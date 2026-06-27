package ddns.net.muchserver.speedometer.viewmodel


import android.app.Application
import android.app.Service
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.MutableStateFlow

import dagger.hilt.android.lifecycle.HiltViewModel
import ddns.net.muchserver.speedometer.location.RepositoryLocation
import ddns.net.muchserver.speedometer.logic.formatSpeed
import ddns.net.muchserver.speedometer.preferences.DataStoreRepository
import ddns.net.muchserver.speedometer.service.ServiceForeground
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ViewModelSpeedometer @Inject constructor(
    private val repositoryLocation: RepositoryLocation,
    application: Application
): AndroidViewModel(application) {
    private val app = application
    private val _currentLocation = MutableStateFlow<Location?>(null)
    val currentLocation = _currentLocation.asStateFlow()

    val speedMax: MutableLiveData<Float> = MutableLiveData(0.0f)
    val resetSpeedMax = { speedMax.value = 0.0f }
    val isStandardUnits: MutableLiveData<Boolean> = MutableLiveData(true)

    init {
        viewModelScope.launch {
            repositoryLocation.getLocationUpdates().collect { location ->
                _currentLocation.value = location
                if(location.speed > speedMax.value!!) {
                    speedMax.value = location.speed
                    updateNotification()
                }
            }
        }

    }

    fun updateNotification() {
        if(ServiceForeground.isServiceRunning) {
            viewModelScope.launch {
                Intent(app, ServiceForeground::class.java).also {
                    it.action = ServiceForeground.Actions.UPDATE.toString()
                    it.putExtra("speedMax", speedMax.value)
                    it.putExtra("isStandardUnits", isStandardUnits.value)
                    app.startService(it)
                }
            }
        }
    }

}