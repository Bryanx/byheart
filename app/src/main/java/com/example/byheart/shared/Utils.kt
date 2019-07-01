package com.example.byheart.shared

import android.app.Activity
import android.graphics.Point

fun getDeviceWidth(ctx: Activity): Int {
    val display = ctx.windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size.x
}