package com.oceanluc.kywheels.activities

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ProgressBar
import com.oceanluc.kywheels.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    private var POLICY_WEB = "https://sites.google.com/view/weewewef/главная-страница"
    private lateinit var privacy: SavedUserPrefs;

    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val button = this.findViewById<View>(R.id.ocean_btn_play)
        val loading = this.findViewById<ProgressBar>(R.id.loading)

        privacy = SavedUserPrefs(this)

        val animator = ValueAnimator.ofInt(0, 100)
        animator.duration = 2350

        animator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Int
            loading.progress = progress
        }

        animator.start()

        Handler(Looper.getMainLooper()).postDelayed({
            button.setOnClickListener {
                if (privacy.privacyCheck) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    val intent = Intent(this, WebPrivacyViewActivity::class.java)
                    intent.putExtra("url", POLICY_WEB)
                    startActivity(intent)
                }
            }
        }, 2350)

    }
}