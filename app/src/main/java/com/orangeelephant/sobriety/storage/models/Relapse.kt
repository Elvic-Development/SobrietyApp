package com.orangeelephant.sobriety.storage.models

data class Relapse(
    val id: Int,
    val counterId: Int,
    val time: Long,
    val comment: String?
)
