package com.orangeelephant.sobriety.util

import android.content.Context
import android.text.format.DateFormat
import com.orangeelephant.sobriety.ApplicationDependencies
import com.orangeelephant.sobriety.R
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.math.abs

data class Duration (
    val years: Long = 0,
    val days: Long = 0,
    val hours: Long = 0,
    val minutes: Long = 0,
    val seconds: Long = 0
)

/**
 * Functions to help out with formatting dates and times within the app
 * using UTC millis
 *
 * Using code from https://github.com/signalapp/Signal-Android/blob/main/app/src/main/java/org/thoughtcrime/securesms/util/DateUtils.java
 */
object DateTimeFormatUtil {
    fun formatDate(context: Context, timestamp: Long): String {
        val locale = context.resources.configuration.locales.get(0)
        val simpleDateFormat = SimpleDateFormat("yyyyMMdd", locale)
        val today = simpleDateFormat.format(System.currentTimeMillis()) == simpleDateFormat.format(timestamp)

        return if (today) {
            context.getString(R.string.today)
        } else {
            val format: String = if (isWithinAbs(timestamp, 6, TimeUnit.DAYS)) {
                "EEE "
            } else if (isWithinAbs(timestamp, 365, TimeUnit.DAYS)) {
                "MMM d"
            } else {
                "MMM d, yyy"
            }

            SimpleDateFormat(format, locale).format(Date(timestamp))
        }
    }

    fun formatTime(context: Context, timestamp: Long): String {
        val locale = context.resources.configuration.locales.get(0)
        val format = if (DateFormat.is24HourFormat(context)) "HH:mm" else "hh:mm a"

        return SimpleDateFormat(format, locale).format(Date(timestamp))
    }

    private fun isWithinAbs(millis: Long, span: Long, unit: TimeUnit): Boolean {
        return abs(System.currentTimeMillis() - millis) <= unit.toMillis(span)
    }

    fun formatDurationAsString(duration: Long): String {
        val durationObject = getDurationFromTimeInMillis(duration)

        return String.format(
            ApplicationDependencies.getApplicationContext().getString(R.string.duration_string),
            durationObject.years,
            durationObject.days,
            durationObject.hours,
            durationObject.minutes,
            durationObject.seconds
        )
    }

    fun getDurationFromStartTime(startTime: Long): Duration {
        return getDurationFromTimeInMillis(Calendar.getInstance().timeInMillis - startTime)
    }

    private fun getDurationFromTimeInMillis(timeMillis: Long): Duration {
        var durationMillis = timeMillis

        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24
        val yearsInMilli = daysInMilli * 365

        val elapsedYears: Long = durationMillis / yearsInMilli
        durationMillis %= yearsInMilli

        val elapsedDays: Long = durationMillis / daysInMilli
        durationMillis %= daysInMilli

        val elapsedHours: Long = durationMillis / hoursInMilli
        durationMillis %= hoursInMilli

        val elapsedMinutes: Long = durationMillis / minutesInMilli
        durationMillis %= minutesInMilli

        val elapsedSeconds: Long = durationMillis / secondsInMilli

        return Duration(
            years = elapsedYears,
            days = elapsedDays,
            hours = elapsedHours,
            minutes = elapsedMinutes,
            seconds = elapsedSeconds
        )
    }
}

/**
 * Convert milliseconds to zoned date time with provided [zoneId].
 */
fun Long.toZonedDateTime(zoneId: ZoneId = ZoneId.systemDefault()): ZonedDateTime {
    return ZonedDateTime.ofInstant(Instant.ofEpochMilli(this), zoneId)
}