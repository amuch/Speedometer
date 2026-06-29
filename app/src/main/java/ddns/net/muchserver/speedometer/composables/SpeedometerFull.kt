package ddns.net.muchserver.speedometer.composables

import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.toArgb
import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ddns.net.muchserver.speedometer.R
import ddns.net.muchserver.speedometer.logic.FORMAT_SCALE
import ddns.net.muchserver.speedometer.logic.formatAltitude
import ddns.net.muchserver.speedometer.logic.formatLatitude
import ddns.net.muchserver.speedometer.logic.formatLongitude
import ddns.net.muchserver.speedometer.logic.formatSpeed
import ddns.net.muchserver.speedometer.logic.formatSpeedUnits
import ddns.net.muchserver.speedometer.logic.scaleIncrements
import ddns.net.muchserver.speedometer.logic.scaleMax
import ddns.net.muchserver.speedometer.logic.speedScaled
import ddns.net.muchserver.speedometer.viewmodel.ViewModelSettings
import ddns.net.muchserver.speedometer.viewmodel.ViewModelSpeedometer
import java.text.DecimalFormat
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin



val FONT_SIZE_SPEED_MAIN = 60.sp
val FONT_SIZE_UNITS_MAIN = 35.sp
val FONT_SIZE_SPEED_MAX = 20.sp
val FONT_SIZE_BEARING = 22.sp
val FONT_SIZE_ALTITUDE = 20.sp
val FONT_SIZE_LAT_LONG = FONT_SIZE_ALTITUDE

const val OFFSET_FACTOR_VERTICAL_SPEED = 0.70f
const val OFFSET_FACTOR_VERTICAL_SPEED_MAX = 0.90f
const val OFFSET_FACTOR_VERTICAL_ALTITUDE = 0.13f
const val OFFSET_FACTOR_VERTICAL_LAT_LONG = 0.05f
const val OFFSET_FACTOR_HORIZONTAL_ALTITUDE = 0.03f
const val OFFSET_FACTOR_HORIZONTAL_LATITUDE = OFFSET_FACTOR_HORIZONTAL_ALTITUDE
const val OFFSET_FACTOR_HORIZONTAL_LONGITUDE = OFFSET_FACTOR_HORIZONTAL_LATITUDE + 0.17f

const val OFFSET_FACTOR_VERTICAL_BEARING = OFFSET_FACTOR_VERTICAL_LAT_LONG

const val START_ANGLE = -225f
const val SWEEP_ANGLE_MAX = 270f

@Composable
fun SpeedometerFull(
    modifier: Modifier,
    colors: List<Color>,
    viewModelSpeedometer: ViewModelSpeedometer,
    viewModelSettings: ViewModelSettings
) {
    val location by viewModelSpeedometer.currentLocation.collectAsStateWithLifecycle()
    val isStandardUnits: Boolean by viewModelSettings.standardUnits.observeAsState(true)
    val speedMax: Float by viewModelSpeedometer.speedMax.observeAsState(0.0f)
    var bearing by remember { mutableFloatStateOf(0.0f) }
    if(location?.hasBearing() ?: false) {
        bearing = location!!.bearing
    }

    val textMeasurer = rememberTextMeasurer()
    val fontFamily = remember {
        FontFamily(Font(R.font.robot9000))
    }

    Canvas(
        modifier = modifier
    ) {
        drawBackground(this, colors)
        drawNeedle(this, colors, speedMax, location, isStandardUnits)
        drawSpeed(this, colors, location, textMeasurer, fontFamily, isStandardUnits)
        drawSpeedMax(this, colors, speedMax, textMeasurer, fontFamily, isStandardUnits)
        drawScale(this, colors, speedMax, textMeasurer, fontFamily, isStandardUnits)

        if(viewModelSettings.isFullScreen.value!!) {
            drawBearing(this, colors, bearing, textMeasurer, fontFamily)
            drawLatLong(this, colors, textMeasurer, fontFamily, location)
            drawAltitude(this, colors, textMeasurer, fontFamily, location, isStandardUnits)
        }
    }
}

fun drawNeedle(
    drawScope: DrawScope,
    colors: List<Color>,
    speedMax: Float,
    location: Location?,
    isStandardUnits: Boolean
) {
    val speed = location?.speed ?: 0.0f
    val width = drawScope.size.width
    val height = drawScope.size.height
    val centerX = 0.5f * width
    val centerY = 0.5f * height

    val colorStops = arrayOf(
        0.30f to colors[INDEX_BACKGROUND_BUTTON],
        0.75f to  colors[INDEX_TEXT]
    )
    val brush = Brush.verticalGradient(colorStops = colorStops)

    val sideShorter = min(width, height)
    val segment = sideShorter / 30
    val pathNeedle = Path().apply {
        moveTo(centerX - segment, centerY) // left
        lineTo(centerX, centerY - 6.5f * segment) // top
        lineTo(centerX + segment, centerY) // right
        lineTo(centerX, centerY + 3 * segment) // bottom
        lineTo(centerX - segment, centerY) // left
        close()
    }

    val max = scaleMax(speedMax, isStandardUnits)
    val angleRotation = (speedScaled(speed, isStandardUnits) / max) * 270f - 135f
    drawScope.withTransform({
        translate(top = -0.1f * height)
        rotate(angleRotation)
    }) {
        drawPath(
            path = pathNeedle,
            brush = brush
        )
    }
}

fun drawBearing(
    drawScope: DrawScope,
    colors: List<Color>,
    bearing: Float,
    textMeasurer: TextMeasurer,
    fontFamily: FontFamily,
) {
    val width = drawScope.size.width
    val height = drawScope.size.height
    val sideShorter = min(width, height)
    val radiusArc = 0.15f * sideShorter
    val centerX = 0.5f * width
    val centerY = 0.5f * height

    val offsetVertical = -0.26f * height

    val directions = listOf("N", "E", "S", "W")
    for(i in 0 until directions.size) {
        val textDirection = directions[i]
        val textMeasuredDirection = textMeasurer.measure(
            text = textDirection,
            style = TextStyle(fontSize = FONT_SIZE_BEARING, fontFamily = fontFamily)
        )
        val theta = i * 0.5f * PI.toFloat() + 0.5f * PI.toFloat()
        val x = (centerX - textMeasuredDirection.size.width / 2 - cos(theta) * radiusArc).toFloat()
        val y = (centerY - textMeasuredDirection.size.height / 2 - sin(theta) * radiusArc).toFloat()
        val offsetInterval = Offset(
            x,
            y
        )
        drawScope.withTransform({
            translate(top = offsetVertical, left = 0.40f * width)
        }) {
            drawText(
                textLayoutResult = textMeasuredDirection,
                color = colors[INDEX_TEXT],
                topLeft = offsetInterval
            )
        }

        val colorStops = arrayOf(
            0.30f to colors[INDEX_BACKGROUND_BUTTON],
            0.75f to  colors[INDEX_TEXT]
        )
        val brush = Brush.verticalGradient(colorStops = colorStops)

        val factorArrow = 0.45f
        val factorOffsetVert = -0.15f
        val factorOffestHor = 0.25f

        val offsetTop = Offset(
            x =  centerX,
            y = centerY - radiusArc * factorArrow
        )
        val strokeWidth = 12.0f
        drawScope.withTransform({
            translate(top = offsetVertical, left = 0.40f * width)
            rotate(bearing)
        }) {
            drawLine(
                brush = brush,
                start = Offset(
                    x = centerX,
                    y = centerY + radiusArc * factorArrow
                ),
                end = offsetTop,
//                color = colors[INDEX_TEXT],
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )

            drawLine(
                brush = brush,
                start = offsetTop,
                end = Offset(
                    x = centerX - radiusArc * factorOffestHor,
                    y = centerY + radiusArc * factorOffsetVert
                ),
//                color = colors[INDEX_TEXT],
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )

            drawLine(
                brush = brush,
                start = offsetTop,
                end = Offset(
                    x = centerX + radiusArc * factorOffestHor,
                    y = centerY + radiusArc * factorOffsetVert
                ),
//                color = colors[INDEX_TEXT],
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
        }
    }
}

fun drawScale(
    drawScope: DrawScope,
    colors: List<Color>,
    speedMax: Float,
    textMeasurer: TextMeasurer,
    fontFamily: FontFamily,
    isStandardUnits: Boolean
) {
    val width = drawScope.size.width
    val height = drawScope.size.height
    val sideShorter = min(width, height)
    val maxScaled = scaleMax(speedMax, isStandardUnits)

    val centerX = 0.5f * width
    val centerY = 0.4f * height
    val radiusArc = 0.3f * sideShorter
    val dashMax = maxScaled.toInt()
    val scaleDetails = scaleIncrements(maxScaled)
    for(i in 0 .. dashMax step scaleDetails.increment) {
        val theta = 1.5f * PI.toFloat() * i / dashMax - 0.25f * PI.toFloat()
        val lengthDivisor = if(i % scaleDetails.dashModulus == 0) 1.28f else 1.2f
        val strokeWidth = if(i % scaleDetails.dashModulus == 0) 9.0f else 8.0f
        drawScope.drawLine(
            color = colors[INDEX_TEXT],
            start = Offset(
                x = centerX - cos(theta) * (radiusArc / lengthDivisor),
                y = centerY - sin(theta) * (radiusArc / lengthDivisor)
            ),
            end = Offset(
                x = centerX - cos(theta) * radiusArc,
                y = centerY - sin(theta) * radiusArc
            ),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
    val topLeft = Offset(
        x = centerX - radiusArc,
        y = centerY - radiusArc
    )
    val size = Size(
        width = 2 * radiusArc,
        height = 2 * radiusArc
    )
    drawScope.drawArc(
        color = colors[INDEX_TEXT],
        startAngle = START_ANGLE,
        sweepAngle = SWEEP_ANGLE_MAX,
        useCenter = false,
        topLeft = topLeft,
        size = size,
        style = Stroke(
            width = 18.0f,
            cap = StrokeCap.Round
        )
    )
    drawScope.drawArc(
        color = colors[INDEX_BACKGROUND_BUTTON],
        startAngle = START_ANGLE,
        sweepAngle = SWEEP_ANGLE_MAX * speedScaled(speedMax, isStandardUnits) / maxScaled,
        useCenter = false,
        topLeft = topLeft,
        size = size,
        style = Stroke(
            width = 15.0f,
            cap = StrokeCap.Round
        )
    )

    val intervalMax = (maxScaled / scaleDetails.dashModulus).toInt()
    val offsetMultiplier = 1.18f
    for(i in 0 .. intervalMax) {
        val textInterval = FORMAT_SCALE.format(maxScaled * i / intervalMax).toString()
        val textMeasuredInterval = textMeasurer.measure(
            textInterval,
            style = TextStyle(fontSize = FONT_SIZE_SPEED_MAX, fontFamily = fontFamily)
        )
        val theta = 1.5f * PI.toFloat() * i / intervalMax - 0.25f * PI.toFloat()
        val x = centerX - textMeasuredInterval.size.width / 2 - cos(theta) * radiusArc * offsetMultiplier
        val y = centerY - textMeasuredInterval.size.height / 2 - sin(theta) * radiusArc * offsetMultiplier
        val offsetInterval = Offset(
            x,
            y
        )
        drawScope.drawText(
            textLayoutResult = textMeasuredInterval,
            color = colors[INDEX_TEXT],
            topLeft = offsetInterval
        )
    }
}

fun drawSpeedMax(
    drawScope: DrawScope,
    colors: List<Color>,
    speedMax: Float,
    textMeasurer: TextMeasurer,
    fontFamily: FontFamily,
    isStandardUnits: Boolean
) {
    val speedMaxFormatted = formatSpeed(speedMax, isStandardUnits)
    val textMeasuredSpeedMax = textMeasurer.measure(
        speedMaxFormatted,
        style = TextStyle(fontSize = FONT_SIZE_SPEED_MAX, fontFamily = fontFamily)
    )
    val offsetSpeedMax = Offset(
        x = drawScope.size.width / 2 - textMeasuredSpeedMax.size.width,
        y = drawScope.size.height * OFFSET_FACTOR_VERTICAL_SPEED_MAX
    )
    drawScope.drawText(
        textLayoutResult = textMeasuredSpeedMax,
        color = colors[INDEX_TEXT],
        topLeft = offsetSpeedMax
    )

    val textUnitsMax = formatSpeedUnits(isStandardUnits)
    val textMeasuredUnitsMax = textMeasurer.measure(
        text = "$textUnitsMax max",
        style = TextStyle(fontSize = FONT_SIZE_SPEED_MAX, fontFamily = fontFamily)
    )
    val offsetUnits = Offset(
        x = drawScope.size.width / 2,
        y = drawScope.size.height * OFFSET_FACTOR_VERTICAL_SPEED_MAX
    )
    drawScope.drawText(
        textLayoutResult = textMeasuredUnitsMax,
        color = colors[INDEX_TEXT],
        topLeft = offsetUnits
    )
}

fun drawSpeed(
    drawScope: DrawScope,
    colors: List<Color>,
    location: Location?,
    textMeasurer: TextMeasurer,
    fontFamily: FontFamily,
    isStandardUnits: Boolean
) {
    val speed = location?.speed ?: 0.0f
    val speedFormatted = formatSpeed(speed, isStandardUnits)
    val textMeasuredSpeed = textMeasurer.measure(
        speedFormatted,
        style = TextStyle(fontSize = FONT_SIZE_SPEED_MAIN, fontFamily = fontFamily)
    )
    val offsetSpeed = Offset(
        x = drawScope.size.width / 2 - textMeasuredSpeed.size.width,
        y = drawScope.size.height * OFFSET_FACTOR_VERTICAL_SPEED
    )
    drawScope.drawText(
        textLayoutResult = textMeasuredSpeed,
        color = colors[INDEX_TEXT],
        topLeft = offsetSpeed
    )

    val textUnits = formatSpeedUnits(isStandardUnits)
    val textMeasuredUnits = textMeasurer.measure(
        text = textUnits,
        style = TextStyle(fontSize = FONT_SIZE_UNITS_MAIN, fontFamily = fontFamily)
    )
    val offsetUnits = Offset(
        x = drawScope.size.width / 2,
        y = drawScope.size.height * OFFSET_FACTOR_VERTICAL_SPEED + 0.30f * textMeasuredSpeed.size.height
    )
    drawScope.drawText(
        textLayoutResult = textMeasuredUnits,
        color = colors[INDEX_TEXT],
        topLeft = offsetUnits
    )
}

fun drawLatLong(
    drawScope: DrawScope,
    colors: List<Color>,
    textMeasurer: TextMeasurer,
    fontFamily: FontFamily,
    location: Location?
) {
    val latitudeFormatted = formatLatitude(location?.latitude ?: 0.0)
    val longitudeFormatted = formatLongitude(location?.longitude ?: 0.0)
    val textMeasuredLatitude = textMeasurer.measure(
        latitudeFormatted,
        style = TextStyle(fontSize = FONT_SIZE_LAT_LONG, fontFamily = fontFamily)
    )
    val textMeasuredLongitude = textMeasurer.measure(
        longitudeFormatted,
        style = TextStyle(fontSize = FONT_SIZE_LAT_LONG, fontFamily = fontFamily)
    )
    val offsetLatitude = Offset(
        x = drawScope.size.width * OFFSET_FACTOR_HORIZONTAL_LATITUDE,
        y = drawScope.size.height * OFFSET_FACTOR_VERTICAL_LAT_LONG
    )
    val offsetLongitude = Offset(
        x = drawScope.size.width * OFFSET_FACTOR_HORIZONTAL_LONGITUDE,
        y = drawScope.size.height * OFFSET_FACTOR_VERTICAL_LAT_LONG
    )
    drawScope.drawText(
        textLayoutResult = textMeasuredLatitude,
        color = colors[INDEX_TEXT],
        topLeft = offsetLatitude
    )
    drawScope.drawText(
        textLayoutResult = textMeasuredLongitude,
        color = colors[INDEX_TEXT],
        topLeft = offsetLongitude
    )
}

fun drawAltitude(
    drawScope: DrawScope,
    colors: List<Color>,
    textMeasurer: TextMeasurer,
    fontFamily: FontFamily,
    location: Location?,
    isStandardUnits: Boolean
) {
    val altitudeFormatted = formatAltitude(location?.altitude ?: 0.0, isStandardUnits)
    val textMeasuredAltitude = textMeasurer.measure(
        altitudeFormatted,
        style = TextStyle(fontSize = FONT_SIZE_ALTITUDE, fontFamily = fontFamily)
    )
    val offsetAltitude = Offset(
        x = drawScope.size.width * OFFSET_FACTOR_HORIZONTAL_ALTITUDE,
        y = drawScope.size.height * OFFSET_FACTOR_VERTICAL_ALTITUDE
    )
    drawScope.drawText(
        textLayoutResult = textMeasuredAltitude,
        color = colors[INDEX_TEXT],
        topLeft = offsetAltitude
    )
}

fun drawBackground(
    drawScope: DrawScope,
    colors: List<Color>
) {
    val radius = 0.01f * drawScope.size.width
    drawScope.drawRoundRect(
        color = colors[INDEX_BACKGROUND],
        topLeft = Offset(0f, 0f),
        cornerRadius = CornerRadius(
            x = radius,
            y = radius
        )
    )
}


