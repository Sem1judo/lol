package com.oceanluc.kywheels.models

data class MinerModel(
    val image: Int,
    val win: Double,
    val isWin: Boolean = false,
    val miner: String = "Miner"
)