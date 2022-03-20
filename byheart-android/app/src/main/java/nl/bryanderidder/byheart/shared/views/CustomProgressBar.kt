package nl.bryanderidder.byheart.shared.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ProgressBar
import android.widget.RelativeLayout
import nl.bryanderidder.byheart.shared.Preferences
import nl.bryanderidder.byheart.shared.setBrightness
import nl.bryanderidder.byheart.shared.setColor


/**
 * CustomProgressBar
 * @author Bryan de Ridder
 */
class CustomProgressBar(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {

    private val progressBar: ProgressBar = ProgressBar(context)

    init {
        val layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.addRule(CENTER_IN_PARENT, TRUE)
        progressBar.layoutParams = layoutParams
        addView(progressBar, layoutParams)
    }

    fun setCircleColor(color: Int) {
        if (Preferences.DARK_MODE)
            progressBar.setColor(color)
        else
            progressBar.setColor(color.setBrightness(0.55F))
    }

    fun show() = animate().alpha(1F)

    fun hide() = animate().alpha(0F)

}