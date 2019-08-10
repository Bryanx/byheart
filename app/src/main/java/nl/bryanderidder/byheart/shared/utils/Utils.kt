package nl.bryanderidder.byheart.shared.utils

import android.app.Activity
import android.content.res.Resources
import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.Snackbar


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

fun showSnackBar(activity: Activity, message: String) {
    val rootView = activity.window.decorView.findViewById<View>(android.R.id.content)
    Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)
        .setTextColor(Color.WHITE)
        .show()
}

fun showSnackBar(v: View, message: String) {
    Snackbar.make(v, message, Snackbar.LENGTH_SHORT)
        .setTextColor(Color.WHITE)
        .show()
}
