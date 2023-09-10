package com.orangeelephant.sobriety.storage.models

import com.orangeelephant.sobriety.util.DateTimeFormatUtil
import java.util.Calendar

data class Counter(
    val id: Int,
    val name: String,
    val startTimeMillis: Long,
    val recordTimeSoberInMillis: Long,
    val initialStartTime: Long?,
    val creationTime: Long,
    val currentDurationString: String = DateTimeFormatUtil.formatDurationAsString(
        Calendar.getInstance().timeInMillis - startTimeMillis
    )
)
