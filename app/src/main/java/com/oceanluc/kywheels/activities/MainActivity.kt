package com.oceanluc.kywheels.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.oceanluc.kywheels.R
import com.oceanluc.kywheels.tools.PreferencesHelper
import com.oceanluc.kywheels.fragments.menu.AuthGamesFragment
import com.oceanluc.kywheels.fragments.menu.SignUpFragment

class MainActivity : AppCompatActivity() {

    private lateinit var privacy: SavedUserPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        privacy = SavedUserPrefs(this)


        Log.d("MainActivityMainActivityMainActivity", "this is main acti")

        if (savedInstanceState == null) {
            val preferencesHelper = PreferencesHelper(this)
            if (preferencesHelper.isReg) {
                switchToFragment(AuthGamesFragment())
            } else {
                switchToFragment(SignUpFragment())
            }
        }
    }

    private fun switchToFragment(fr: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fr)
            .commit()
    }
}
