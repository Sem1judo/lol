package com.oceanluc.kywheels.models

import com.oceanluc.kywheels.tools.IDiffModel

data class SelectedGamesModel(
    override val image: Int,
    val isOpen: Boolean, val boolean: Boolean = true,
    val model: String = "new"
) : IDiffModel<Int>
