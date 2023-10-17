package com.oceanluc.kywheels.fragments.games.slots

import android.content.Context
import android.content.pm.ActivityInfo
import android.view.Surface
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView?.getCurrentPosition(): Int {
    (this?.layoutManager as LinearLayoutManager).run {
        val first = this.findFirstVisibleItemPosition()

        val screenOrientation = getScreenOrientation(context)

        if (screenOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT || screenOrientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) {
            return first + 1
        } else {
            val first_c = this.findFirstCompletelyVisibleItemPosition()
            return if (first == first_c)
                first + 1
            else
                first + 2
        }
    }
}

private fun getScreenOrientation(context: Context): Int {
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return when (windowManager.defaultDisplay.rotation) {
        Surface.ROTATION_0 -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        Surface.ROTATION_90 -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        Surface.ROTATION_180 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
        Surface.ROTATION_270 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
        else -> ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}