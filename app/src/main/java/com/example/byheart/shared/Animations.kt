package com.example.byheart.shared

import android.graphics.Point
import android.view.View
import android.view.WindowManager
import android.view.animation.TranslateAnimation


// slide the view from below itself to the current position
fun moveX(view: View, from: Float, to: Float): TranslateAnimation {
    view.visibility = View.VISIBLE
    val animate = TranslateAnimation(
        from, // fromXDelta
        to, // toXDelta
        0f, // fromYDelta
        0f
    )                // toYDelta
    animate.duration = 300
    animate.fillAfter = true
    view.startAnimation(animate)
    return animate
}

fun getScreenWidth(windowManager: WindowManager?): Float {
    val display = windowManager?.defaultDisplay
    val size = Point()
    display?.getSize(size)
    return size.x.toFloat()
}