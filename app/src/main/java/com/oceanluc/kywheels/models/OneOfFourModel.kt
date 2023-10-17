package com.oceanluc.kywheels.models

data class OneOfFourModel(
    val image: Int,
    val bid: Double,
    val currentItem: Int,
    val isUser: Boolean = false,
    val model: String = "model"
)