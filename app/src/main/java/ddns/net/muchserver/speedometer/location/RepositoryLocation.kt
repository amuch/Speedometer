package ddns.net.muchserver.speedometer.location

import android.annotation.SuppressLint
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import ddns.net.muchserver.speedometer.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton


const val INTERVAL_LOCATION_REQUEST = 500L
const val INTERVAL_LOCATION_REQUEST_FASTEST = 500L
const val LAT_LNG_DEFAULT = 0.0


@Singleton
class RepositoryLocation @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient
){
    suspend fun getCurrentLocation(
        priority: Int = Priority.PRIORITY_HIGH_ACCURACY,
        maxUpdateAgeMilliseconds: Long = 60_000L
    ): Result<Location> = runCatching {
        @SuppressLint("MissingPermission")
        val location: Location? = fusedLocationProviderClient
            .getCurrentLocation(priority, CancellationTokenSource().token)
            .await()

        if(location == null) {
            throw Exception("Location not available")
        }

        location
    }
    .onFailure { throwable ->
        println("getCurrentLocation failed ${throwable.message}")
    }
    fun getLocationUpdates(
        locationRequest: LocationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            INTERVAL_LOCATION_REQUEST
        )
        .setMinUpdateIntervalMillis(INTERVAL_LOCATION_REQUEST_FASTEST)
        .setMaxUpdateDelayMillis(INTERVAL_LOCATION_REQUEST)
        .build()
    ): Flow<Location> = callbackFlow {
//        if(!MainActivity.permissionsGranted) {
//            println("Permission not granted")
//            return@callbackFlow
//        }
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { trySend(it) }
            }
        }

        @SuppressLint("MissingPermission")
        val task = fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
        task.addOnSuccessListener {
            println("Successful update")
        }
        task.addOnFailureListener {
            println("Failed to update")
        }
        awaitClose {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }
    .catch {  throwable ->
        println("${throwable.message}")
    }
    .flowOn(Dispatchers.IO)


}

//interface PermissionChecker {
//    suspend fun hasFineLocation(): Boolean
//    suspend fun hasBackgroundLocation(): Boolean
//    suspend fun requestFineLocationIfNeeded(activity: ComponentActivity): Boolean
//    // or more advanced: flow of permission state
//}

/*
   suspend fun getCurrentLocation(
       priority: Int = Priority.PRIORITY_HIGH_ACCURACY
   ): Result<Location> = runCatching {
//        if (!permissionChecker.hasFineLocation()) {
//            return Result.failure(SecurityException("Missing location permission"))
//        }
       fusedLocationProviderClient
           .getCurrentLocation(priority, CancellationTokenSource().token)
           .await()
           .also { location ->
               if(location == null) throw LocationNotAvailableException
           }
   }

   fun getLocationUpdates(
       request: LocationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10_000L)
           .setMinUpdateIntervalMillis(5_000L)
           .setMaxUpdateDelayMillis(30_000L)
           .build()
   ): Flow<Location> = callbackFlow {

       val callback = object: LocationCallback() {
           override fun onLocationResult(locationResult: LocationResult) {
               locationResult.lastLocation?.let { trySend(it) }
               locationResult.locations.forEach { trySend(it) }
           }

           override fun onLocationAvailability(locationAvailability: LocationAvailability) {
               if(!locationAvailability.isLocationAvailable) {
                      // https://grok.com/c/64fe00a1-a220-4c51-8fc5-05b7a1fda703?rid=bd6af1d1-b32f-48a8-a968-a767cd17df03
               }
           }
       }

   }

    */