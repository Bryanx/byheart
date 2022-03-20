package nl.bryanderidder.byheart.shared.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * Relativelayout that maintains a square (based on width)
 * @author Bryan de Ridder
 */
class SquareRelativeLayout(ctx: Context, attrs: AttributeSet) : FrameLayout(ctx, attrs) {

    //Square based on width
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}