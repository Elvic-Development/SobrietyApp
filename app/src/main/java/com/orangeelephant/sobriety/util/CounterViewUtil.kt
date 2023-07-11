package com.orangeelephant.sobriety.util

import com.orangeelephant.sobriety.ApplicationDependencies
import com.orangeelephant.sobriety.R

object CounterViewUtil {
    fun formatDurationAsString(duration: Long): String {
        var durationMillis = duration

        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24

        val elapsedDays: Long = durationMillis / daysInMilli
        durationMillis %= daysInMilli

        val elapsedHours: Long = durationMillis / hoursInMilli
        durationMillis %= hoursInMilli

        val elapsedMinutes: Long = durationMillis / minutesInMilli
        durationMillis %= minutesInMilli

        val elapsedSeconds: Long = durationMillis / secondsInMilli

        return String.format(
            ApplicationDependencies.getApplicationContext().getString(R.string.duration_string),
            elapsedDays,
            elapsedHours,
            elapsedMinutes,
            elapsedSeconds
        )
    }
}