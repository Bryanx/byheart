package nl.bryanderidder.byheart.pile.edit

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable


class ColorStateDrawable(layers: Array<Drawable>, private val color: Int) : LayerDrawable(layers) {

    private val PRESSED_STATE_MULTIPLIER = 0.70f

    override fun onStateChange(states: IntArray): Boolean {
        var pressedOrFocused = false
        for (state in states) {
            if (state == android.R.attr.state_pressed || state == android.R.attr.state_focused) {
                pressedOrFocused = true
                break
            }
        }
        when {
            pressedOrFocused -> super.setColorFilter(getFocusColor(color), PorterDuff.Mode.SRC_ATOP)
            else -> super.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
        return super.onStateChange(states)
    }

    private fun getFocusColor(color: Int): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[2] = hsv[2] * PRESSED_STATE_MULTIPLIER
        return Color.HSVToColor(hsv)
    }

    override fun isStateful() = true

}