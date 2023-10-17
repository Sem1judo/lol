package com.oceanluc.kywheels.activities

import android.content.SharedPreferences
import kotlin.reflect.KProperty

class BooleanPreference(
    private val sharedPref: SharedPreferences,
    private val key: String,
    private val defaultValue: Boolean
) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Boolean {
        return sharedPref.getBoolean(key, defaultValue)
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) {
        sharedPref.edit().putBoolean(key, value).apply()
    }
}