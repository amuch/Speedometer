package ddns.net.muchserver.speedometer

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import ddns.net.muchserver.speedometer.composables.MainScreen
import ddns.net.muchserver.speedometer.service.ServiceForeground
import ddns.net.muchserver.speedometer.ui.theme.SpeedometerTheme
import ddns.net.muchserver.speedometer.viewmodel.FactoryViewModelTrip
import ddns.net.muchserver.speedometer.viewmodel.ViewModelPreferences
import ddns.net.muchserver.speedometer.viewmodel.ViewModelSettings
import ddns.net.muchserver.speedometer.viewmodel.ViewModelSpeedometer
import ddns.net.muchserver.speedometer.viewmodel.ViewModelTrip

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var viewModelSettings: ViewModelSettings
    lateinit var viewModelPreferences: ViewModelPreferences
    lateinit var factoryViewModelTrip: FactoryViewModelTrip
    lateinit var viewModelTrip: ViewModelTrip
    val viewModelSpeedometer: ViewModelSpeedometer by viewModels()

    companion object {
        var permissionsGranted = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeViewModels()

        activityResultLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)

        enableEdgeToEdge()
        setContent {
            SpeedometerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier
                            .padding(innerPadding),
                        activity = this,
                        viewModelSettings = viewModelSettings,
                        viewModelPreferences = viewModelPreferences,
                        viewModelSpeedometer = viewModelSpeedometer,
                        viewModelTrip = viewModelTrip
                    )
                }
            }
        }
    }

    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            isGranted ->
        if(isGranted) {
            permissionsGranted = true
        }
        else {
            Toast.makeText(applicationContext, R.string.location_services_denied, Toast.LENGTH_LONG).show()
        }
    }

    private fun initializeViewModels() {
        factoryViewModelTrip = FactoryViewModelTrip(this.application as Application)
        viewModelTrip = ViewModelProvider(this, factoryViewModelTrip)[ViewModelTrip::class.java]

        viewModelSettings = ViewModelProvider(this)[ViewModelSettings::class.java]
        viewModelSettings.isScreenAwake.observe(this) {awake ->
            if(awake) {
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
            else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
        viewModelPreferences = ViewModelProvider(this)[ViewModelPreferences::class.java]
        viewModelPreferences.readFromDataStore.observe(this) { preferences ->
            viewModelSettings.setStandardUnits(preferences.standardUnits)
            viewModelSettings.setSchemeColor(preferences.theme, preferences.indexTheme)

            if(preferences.standardUnits != viewModelSpeedometer.isStandardUnits.value) {
                viewModelSpeedometer.isStandardUnits.value = preferences.standardUnits
                viewModelSpeedometer.updateNotification()
            }
        }
    }
}
