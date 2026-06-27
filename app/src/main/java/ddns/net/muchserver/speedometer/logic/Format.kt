package ddns.net.muchserver.speedometer.logic


import java.text.DecimalFormat
import kotlin.Int

const val MPS_TO_MPH = 2.236936f
const val MPS_TO_KPH = 3.6f
val DECIMAL_FORMAT_TENTHS = DecimalFormat("#.#")
val DECIMAL_FORMAT_HUNDREDTHS = DecimalFormat("#.##")
val FORMAT_DECIMAL = DecimalFormat("#.##")
val FORMAT_SCALE = DecimalFormat("#")
val FORMAT_ORDINATE = DecimalFormat("#.####")

const val CONVERSION_METERS_TO_MILES = 0.0006213712f
const val CONVERSION_METERS_TO_FEET = 3.28084f
const val CONVERSION_METERS_TO_KM = 0.001f

val SCALE_MPH = listOf(
    4f,
    8f,
    15f,
    30f,
    60f,
    120f,
    250f
)

val SCALE_KPH = listOf(
    10f,
    20f,
    30f,
    50f,
    100f,
    200f,
    400f
)

fun formatAltitude(altitude: Double, standardUnits: Boolean): String {
    if(standardUnits) {
        return "${DECIMAL_FORMAT_TENTHS.format(altitude * CONVERSION_METERS_TO_FEET)} ft"
    }

    return "${DECIMAL_FORMAT_TENTHS.format(altitude)} m"
}

fun formatSpeed(speed: Float, isStandardUnits: Boolean): String {
    val multiplier = if(isStandardUnits) MPS_TO_MPH else MPS_TO_KPH
    return "${DECIMAL_FORMAT_TENTHS.format(speed * multiplier)}"
}

fun formatSpeedUnits(isStandardUnits: Boolean): String {
    return if(isStandardUnits) " MPH" else " KPH"
}

fun formatDistance(distance: Double, standardUnits: Boolean): String {
    if(standardUnits) {
        return "${DECIMAL_FORMAT_HUNDREDTHS.format(distance * CONVERSION_METERS_TO_MILES)} mi"
    }

    return "${DECIMAL_FORMAT_HUNDREDTHS.format(distance * CONVERSION_METERS_TO_KM)} km"
}

fun speedScaled(speed: Float, isStandardUnits: Boolean): Float {
    if(isStandardUnits) {
        return speed * MPS_TO_MPH
    }
    return speed * MPS_TO_KPH
}

fun scaleMax(speed: Float, isStandardUnits: Boolean): Float {
    val scaled = speedScaled(speed, isStandardUnits)
    val scales = if(isStandardUnits) SCALE_MPH else SCALE_KPH
    return scales.firstOrNull { scaled < it } ?: scales.last()
}

fun scaleIncrements(max: Float): ScaleDetails {
    when(max) {
        400f -> return  ScaleDetails(10, 80)
        250f -> return ScaleDetails(10, 50)
        200f -> return ScaleDetails(10, 40)
        120f -> return ScaleDetails(5, 20)
        100f, 60f, 50f -> return  ScaleDetails(1, 10)
        30f -> return ScaleDetails(1, 6)
        20f -> return ScaleDetails(1, 4)
        15f -> return ScaleDetails(1, 3)
        10f -> return ScaleDetails(1, 2)
        8f, 4f -> return ScaleDetails(1, 1)
        else -> return ScaleDetails(1, 1)
    }
}

fun scaleValuesFromMax(max: Float): List<Float> {
    when(max) {
        4f -> return listOf(0f, 1f, 2f, 3f, 4f)
        8f -> return listOf(0f, 2f, 4f, 6f, 8f)
        10f -> return listOf(0f, 2f, 4f, 6f, 8f, 10f)
        15f -> return listOf(0f, 3f, 6f, 9f, 12f, 15f)
        20f -> return listOf(0f, 4f, 8f, 12f, 16f, 20f)
        30f -> return listOf(0f, 6f, 12f, 18f, 24f, 30f)
        50f -> return listOf(0f, 10f, 20f, 30f, 40f, 50f)
        60f -> return listOf(0f, 10f, 20f, 30f, 40f, 50f, 60f)
        100f -> return listOf(0f, 10f, 20f, 30f, 40f, 50f, 60f, 70f, 80f, 90f, 100f)
        120f -> return listOf(0f, 20f, 40f, 60f, 80f, 100f, 120f)
        200f -> return listOf(0f, 40f, 80f, 120f, 140f, 160f, 180f, 200f)
        250f -> return listOf(0f, 50f, 100f, 150f, 200f, 250f)
        400f -> return listOf(0f, 80f, 160f, 240f, 320f, 400f)
    }
    
    return emptyList()
}

fun formatLatitude(latitude: Double): String {
    if(latitude < 0) {
        return "${FORMAT_ORDINATE.format(-1 * latitude)} S"
    }

    return "${FORMAT_ORDINATE.format(latitude)} N"
}

fun formatLongitude(longitude: Double): String {
    if(longitude < 0) {
        return "${FORMAT_ORDINATE.format(-1 * longitude)} W"
    }
    return "${FORMAT_ORDINATE.format(longitude)} E"
}

fun calculateDirection(bearing: Float): String {
    if(bearing > 315) {
        return "NW"
    }
    if(bearing > 270) {
        return "W"
    }
    if(bearing > 225) {
        return "SW"
    }
    if(bearing > 180) {
        return "S"
    }
    if(bearing > 135) {
        return "SE"
    }
    if(bearing > 90) {
        return "E"
    }
    if(bearing > 45) {
        return "NE"
    }

    return "N"
}

data class ScaleDetails(val increment: Int, val dashModulus: Int)