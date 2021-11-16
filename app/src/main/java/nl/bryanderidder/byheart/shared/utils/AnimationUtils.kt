package nl.bryanderidder.byheart.shared.utils

import android.animation.ObjectAnimator
import android.graphics.Point
import android.view.View
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.TranslateAnimation
import androidx.recyclerview.widget.RecyclerView

/**
 * Contains all functions related to animations.
 * @author Bryan de Ridder
 */

// slide the view from below itself to the current position
fun moveX(view: View, from: Float, to: Float): TranslateAnimation {
    view.visibility = View.VISIBLE
    val animate: TranslateAnimation = TranslateAnimation(from, to, 0f, 0f).apply {
        duration = 150
        fillAfter = true
        interpolator = AccelerateDecelerateInterpolator()
    }
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
fun flipY(view: View, from: Float, to: Float, duration: Long): ObjectAnimator {
    val animate = ObjectAnimator.ofFloat(view, "rotationY", from, to)
    animate.duration = duration
    animate.start()
    return animate
}

fun RecyclerView.doAfterAnimations(callback: (RecyclerView) -> Unit) = post(object : Runnable {
        override fun run() {
            if (isAnimating) {
                itemAnimator!!.isRunning { post(this) }
            } else {
                callback(this@doAfterAnimations)
            }
        }
    })