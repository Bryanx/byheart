package nl.bryanderidder.byheart.shared.utils

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.google.android.material.snackbar.Snackbar


/**
 * Contains all utility functions.
 * @author Bryan de Ridder
 */
fun getDeviceWidth(ctx: Activity): Float {
    val displayMetrics = ctx.resources.displayMetrics
    return displayMetrics.widthPixels / displayMetrics.density
}

fun goToUrl(activity: Activity, url: String) {
    val uriUrl = Uri.parse(url)
    val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
    launchBrowser.addCategory(Intent.CATEGORY_BROWSABLE)
    activity.startActivity(launchBrowser)
}

val Int.px: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Int.dp: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()

fun showSnackBar(activity: Activity, message: String, length: Int = Snackbar.LENGTH_SHORT) {
    val rootView = activity.window.decorView.findViewById<View>(android.R.id.content)
    Snackbar.make(rootView, message, length)
        .setTextColor(Color.WHITE)
        .show()
}

fun Fragment.showSnackBar(message: String, actionMessage: String, actionColor: Int, onClick: () -> Unit) {
    val rootView = activity!!.window.decorView.findViewById<View>(android.R.id.content)
    Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
        .setTextColor(Color.WHITE)
        .setActionTextColor(actionColor)
        .setAction(actionMessage) { onClick() }
        .show()
}

fun showSnackBar(v: View, message: String) {
    Snackbar.make(v, message, Snackbar.LENGTH_SHORT)
        .setTextColor(Color.WHITE)
        .show()
}

fun secondsToTimeFormat(seconds: LiveData<Int>): String {
    return String.format("%02d:%02d",((seconds.value?.rem(3600))?.div(60)), (seconds.value?.rem(60)))
}

fun fractionToPercentage(numerator: LiveData<Int>, denominator: Int): Int {
    if (numerator.value == denominator) return 100
    if (numerator.value == 0) return 0
    return ((numerator.value?.toDouble()?.div(denominator.toDouble()) ?: 0.0) * 100).toInt()
}
