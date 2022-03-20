package nl.bryanderidder.byheart.shared.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.shared.Preferences

/**
 * Custom view that decides whether to use a dark or light images
 * based on the whether dark mode is on or not.
 * @author Bryan de Ridder
 */
class DayNightImageView(context: Context, attrs: AttributeSet) : AppCompatImageView(context, attrs) {

    init {
        val styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.DayNightImageView)
        if (Preferences.DARK_MODE) {
            this.background = styledAttrs.getDrawable(R.styleable.DayNightImageView_nightSrc)
        } else {
            this.background = styledAttrs.getDrawable(R.styleable.DayNightImageView_daySrc)
        }
        styledAttrs.recycle()
    }

}