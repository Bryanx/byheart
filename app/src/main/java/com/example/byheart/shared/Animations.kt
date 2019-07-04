package com.example.byheart.shared

import android.graphics.Point
import android.view.View
import android.view.WindowManager
import android.view.animation.TranslateAnimation
import android.view.animation.AccelerateDecelerateInterpolator
import android.animation.ObjectAnimator


// slide the view from below itself to the current position
fun moveX(view: View, from: Float, to: Float): TranslateAnimation {
    view.visibility = View.VISIBLE
    val animate = TranslateAnimation(
        from, // fromXDelta
        to, // toXDelta
        0f,
        0f
    )
    animate.duration = 200
    animate.fillAfter = true
    animate.interpolator = AccelerateDecelerateInterpolator()
    view.startAnimation(animate)
    return animate
}

fun getScreenWidth(windowManager: WindowManager?): Float {
    val display = windowManager?.defaultDisplay
    val size = Point()
    display?.getSize(size)
    return size.x.toFloat()
}

// slide the view from below itself to the current position
fun flipY(view: View, from: Float, to: Float): ObjectAnimator {
    val animate = ObjectAnimator.ofFloat(view, "rotationY", from, to)
    animate.duration = 150
    animate.start()
    return animate
}