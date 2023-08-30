package com.orangeelephant.sobriety.storage.models

data class Relapse(
    val id: Int,
    val counterId: Int,
    val relapseTime: Long,
    val comment: String?,
    val recordedAtTime: Long
)
