package nl.bryanderidder.byheart.shared

import android.app.Activity
import android.content.res.Resources

/**
 * Contains all utility functions.
 * @author Bryan de Ridder
 */
fun getDeviceWidth(ctx: Activity): Float {
    val displayMetrics = ctx.resources.displayMetrics
    return displayMetrics.widthPixels / displayMetrics.density
}

val Int.px: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Int.dp: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()
