package com.orangeelephant.sobriety.storage.models

data class Counter(
    val id: Int,
    val name: String,
    val startTimeMillis: Long,
    val recordTimeSoberInMillis: Long,
)
