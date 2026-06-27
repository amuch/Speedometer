package ddns.net.muchserver.speedometer

import androidx.core.app.ComponentActivity

interface PermissionChecker {
    suspend fun hasPermissionFineLocation(): Boolean
    suspend fun hasPermissionBackgroundLocation(): Boolean
    suspend fun requestFineLocationIfNeeded(activity: ComponentActivity): Boolean
}