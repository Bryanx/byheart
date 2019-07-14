package nl.bryanderidder.byheart.shared.views

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout

/**
 * Simple RelativeLayout that animates upon layout changes.
 * Should have android:animateLayoutChanges="true" as an attribute in xml.
 * @author Bryan de Ridder
 */
class AnimatingRelativeLayout(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {
    init {
        this.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
    }
}