package com.orangeelephant.sobriety.storage.models

import com.orangeelephant.sobriety.util.CounterViewUtil
import java.util.Calendar

data class Counter(
    val id: Int,
    val name: String,
    val startTimeMillis: Long,
    val recordTimeSoberInMillis: Long,
    val currentDurationString: String = CounterViewUtil.formatDurationAsString(
        Calendar.getInstance().timeInMillis - startTimeMillis
    )
)
