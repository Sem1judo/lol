package com.oceanluc.kywheels.activities

import android.content.Context
import android.content.SharedPreferences

class SavedUserPrefs(context: Context) {

    private val userPref: SharedPreferences by lazy {
        context.getSharedPreferences(KEY_GAME_PREF, Context.MODE_PRIVATE)
    }

    var privacyCheck by BooleanPreference(userPref, KEY_PRIVACY, false)

    companion object {
        private const val KEY_PRIVACY = "Privacy"
        private const val KEY_GAME_PREF = "pref"
    }
}