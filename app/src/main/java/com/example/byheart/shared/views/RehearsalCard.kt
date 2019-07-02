package com.example.byheart.shared.views

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.example.byheart.R
import com.example.byheart.shared.Preferences
import com.example.byheart.shared.Preferences.DARK_MODE
import com.example.byheart.shared.getAttr

class RehearsalCard : TextView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        changeCameraDistance()
        changeCardColor()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    private fun changeCardColor() {
        val color = context.getAttr(R.attr.mainHeaderTextColor)
        this.setTextColor(color)
        if (Preferences.read(DARK_MODE, false)) {
            this.setBackgroundResource(R.drawable.shadow_dark)
        } else {
            this.setBackgroundResource(R.drawable.shadow)
        }
    }

    private fun changeCameraDistance() {
        val distance = 8000
        val scale = resources.displayMetrics.density * distance
        this.cameraDistance = scale
    }

}