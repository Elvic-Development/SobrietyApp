package com.orangeelephant.sobriety.storage.models

data class Relapse(
    val id: Int,
    val counterId: Long,
    val time: Long,
    val comment: String?
)
