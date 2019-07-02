package com.example.byheart.shared

import android.app.Activity
import android.graphics.Point
import androidx.fragment.app.Fragment
import com.example.byheart.MainActivity
import com.example.byheart.card.CardFragment
import kotlinx.android.synthetic.main.activity_main.*

fun getDeviceWidth(ctx: Activity): Int {
    val display = ctx.windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size.x
}