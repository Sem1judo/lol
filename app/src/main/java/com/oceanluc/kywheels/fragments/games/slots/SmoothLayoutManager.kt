package com.oceanluc.kywheels.fragments.games.slots

import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
class SmoothLayoutManager(context: Context) : LinearLayoutManager(context) {

    companion object {
        private const val MILLISECONDS_PER_INCH = 150f
    }

    constructor(context: Context, orientation: Int, reverseLayout: Boolean) : this(context) {
        this.orientation = orientation
        this.reverseLayout = reverseLayout
    }

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView,
        state: RecyclerView.State,
        position: Int
    ) {
        val linearSmoothScroller = object : LinearSmoothScroller(recyclerView.context) {
            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                return MILLISECONDS_PER_INCH / displayMetrics.densityDpi
            }
        }
        linearSmoothScroller.targetPosition = position
        startSmoothScroll(linearSmoothScroller)
    }
}
