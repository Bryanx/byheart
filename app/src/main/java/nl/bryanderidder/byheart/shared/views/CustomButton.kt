package nl.bryanderidder.byheart.shared.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.RelativeLayout
import nl.bryanderidder.byheart.shared.Preferences
import nl.bryanderidder.byheart.shared.setBrightness
import nl.bryanderidder.themedtogglebuttongroup.ThemedButton


/**
 * Custom button
 * @author Bryan de Ridder
 */
class CustomButton(context: Context, attrs: AttributeSet) : RelativeLayout(context,attrs) {
    private val btn = ThemedButton(context, attrs)

    init {
        val layoutParams = ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        btn.layoutParams = layoutParams
        addView(btn, layoutParams)
    }

    var textColor = btn.textColor
        set(value) {
            if (Preferences.DARK_MODE)
                btn.textColor = value
            else
                btn.textColor = value.setBrightness(0.55F)
            field = value
        }
}