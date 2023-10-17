package com.oceanluc.kywheels.models

import com.oceanluc.kywheels.tools.IDiffModel


data class AuthUserGamesModel(
    val id: Int,
    override val image: Int,
    val isOpen: Boolean, val isUser: Boolean = false,
    val user: String = "user",
    val balance: Int = 0

) : IDiffModel<Int>