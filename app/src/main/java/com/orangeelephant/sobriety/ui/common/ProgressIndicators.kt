package com.orangeelephant.sobriety.ui.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.orangeelephant.sobriety.util.Duration


@Composable
fun LinearProgressIndicator(
    numSteps: Int,
    currentStep: Int
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val outline = MaterialTheme.colorScheme.outline

    val proportion = animateFloatAsState (
        targetValue = currentStep.toFloat() / numSteps.toFloat(),
        animationSpec = tween(
            durationMillis = 500
        ),
        label = "Animate proportion of progress bar"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .width(250.dp)
            .height(8.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxHeight(0.8f)
                .fillMaxWidth(0.8f)
        ) {
            drawLine(
                color = outline,
                start = Offset(x = 0.dp.toPx(), y = size.height / 2),
                end = Offset(x = size.width, y = size.height / 2),
                strokeWidth = 10.dp.toPx(),
                cap = StrokeCap.Round
            )

            val end = size.width * proportion.value
            drawLine(
                color = primaryColor,
                start = Offset(x = 0.dp.toPx(), y = size.height / 2),
                end = Offset(x = end, y = size.height / 2),
                strokeWidth = 10.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun MileStoneProgressTracker(
    duration: Duration
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val outline = MaterialTheme.colorScheme.outline

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(size = 300.dp)
    ) {
        Canvas(
            modifier = Modifier.size(size = 300.dp)
        ) {

            drawCircle(
                color = outline,
                radius = 300.dp.toPx() / 2,
                style = Stroke(width = 20.dp.toPx(), cap = StrokeCap.Round)
            )

            val angle = duration.seconds * 6f
            drawArc(
                color = primaryColor,
                startAngle = -90f,
                sweepAngle = angle,
                useCenter = false,
                style = Stroke(20.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Row {
            Column(
                Modifier.defaultMinSize(minWidth = 50.dp),
                horizontalAlignment = Alignment.End
            ) {
                if (duration.years > 0) {
                    Text(
                        "${duration.years}",
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                if (duration.days > 0 || duration.years > 0) {
                    Text(
                        "${duration.days}",
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Row {
                    Text(
                        "${duration.hours}",
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                if (duration.years <= 0) {
                    Text(
                        "${duration.minutes}",
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                if (duration.days <= 0 && duration.years <= 0) {
                    Text(
                        "${duration.seconds}",
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.width(5.dp))
            Column(horizontalAlignment = Alignment.Start) {
                if (duration.years > 0) {
                    Text(
                        "YEARS",
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                if (duration.days > 0 || duration.years > 0) {
                    Text(
                        "DAYS",
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                Text(
                    "HOURS",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.outline
                )

                if (duration.years <= 0) {
                    Text(
                        "MINS",
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                if (duration.days <= 0 && duration.years <= 0) {
                    Text(
                        "SECS",
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun MileStoneProgressCounterPreview() {
    MileStoneProgressTracker(
        duration = Duration(days = 3, seconds = 20)
    )
}

@Preview
@Composable
fun LinearProgressIndicatorPreview() {
    LinearProgressIndicator(numSteps = 3, currentStep = 2)
}