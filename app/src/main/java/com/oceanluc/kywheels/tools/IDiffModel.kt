package com.oceanluc.kywheels.tools

interface IDiffModel<T> {
    val image: T
    override fun equals(other: Any?): Boolean
}