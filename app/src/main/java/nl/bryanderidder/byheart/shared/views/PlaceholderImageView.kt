package nl.bryanderidder.byheart.shared.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.shared.Preferences
import nl.bryanderidder.byheart.shared.Preferences.DARK_MODE

/**
 * Custom view that decides whether to use a dark or light placeholder images
 * based on the whether dark mode is on or not.
 * @author Bryan de Ridder
 */
class PlaceholderImageView(context: Context, attrs: AttributeSet) : AppCompatImageView(context, attrs) {

    init {
        if (Preferences.read(DARK_MODE)) {
            this.setImageResource(R.drawable.ic_study_dark)
        } else {
            this.setImageResource(R.drawable.ic_placeholder_light)
        }
    }

}