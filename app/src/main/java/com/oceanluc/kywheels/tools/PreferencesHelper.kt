package com.oceanluc.kywheels.tools

import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PreferencesHelper(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)

    var isReg: Boolean by booleanPreference("isReg", false)
    var balance: Int by intPreference("balance", 15000)
    var win: Int by intPreference("win", 0)
    var isSound: Boolean by booleanPreference("isSound", false)
    var isVibration: Boolean by booleanPreference("isVib", false)
    var sound: Int by intPreference("sound", 0)
    var vibration: Int by intPreference("vib", 0)
    var level: Int by intPreference("level", 1)
    var balanceAnon: Int by intPreference("balanceAnon", 15000)
    var isPrivacy: Boolean by booleanPreference("isPrivacy", false)

    private inline fun <reified T> SharedPreferences.preference(key: String, defaultValue: T): ReadWriteProperty<Any, T> {
        return object : ReadWriteProperty<Any, T> {
            override fun getValue(thisRef: Any, property: KProperty<*>): T {
                return when (T::class) {
                    Boolean::class -> getBoolean(key, defaultValue as Boolean) as T
                    Int::class -> getInt(key, defaultValue as Int) as T
                    else -> throw IllegalArgumentException("Unsupported type")
                }
            }

            override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
                edit().apply {
                    when (value) {
                        is Boolean -> putBoolean(key, value)
                        is Int -> putInt(key, value)
                        else -> throw IllegalArgumentException("Unsupported type")
                    }
                }.apply()
            }
        }
    }

    private inline fun <reified T> booleanPreference(key: String, defaultValue: T): ReadWriteProperty<Any, T> {
        return sharedPreferences.preference(key, defaultValue)
    }

    private inline fun <reified T> intPreference(key: String, defaultValue: T): ReadWriteProperty<Any, T> {
        return sharedPreferences.preference(key, defaultValue)
    }
}
