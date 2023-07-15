package com.orangeelephant.sobriety.util

import com.orangeelephant.sobriety.ApplicationDependencies
import com.orangeelephant.sobriety.R
import java.util.Calendar

data class Duration (
    val years: Long = 0,
    val days: Long = 0,
    val hours: Long = 0,
    val minutes: Long = 0,
    val seconds: Long = 0
)

object CounterViewUtil {
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