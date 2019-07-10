package com.example.byheart.shared

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.graphics.Point
import android.os.Build
import android.util.TypedValue

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

fun dpToPx(context: Context, valueInDp: Float): Float {
    val metrics = context.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics)
}

fun startNewMainActivity(currentActivity: Activity, newTopActivityClass: Class<out Activity>) {
    val intent = Intent(currentActivity, newTopActivityClass)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TASK)
    currentActivity.startActivity(intent)
}