package nl.bryanderidder.byheart.shared

import android.app.Activity
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.graphics.Point

/**
 * Contains all utility functions.
 * @author Bryan de Ridder
 */
fun getDeviceWidth(ctx: Activity): Int {
    val display = ctx.windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size.x
}