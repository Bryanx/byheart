package nl.bryanderidder.byheart.rehearsal.views

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import nl.bryanderidder.byheart.shared.string

/**
 * CardView that holds a TextView
 * @author Bryan de Ridder
 */
class MultipleChoiceAnswer(ctx: Context, attrs: AttributeSet) : CardView(ctx, attrs) {

    private lateinit var tvText: TextView

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) elevation = 0.5F
        useCompatPadding = true
    }

    var textColor: Int
        get() = tvText.currentTextColor
        set(textColor) = tvText.setTextColor(textColor)

    var text: String
        get() = tvText.string
        set(text) {
            tvText.text = text
        }

    var bgColor: Int
        get() = cardBackgroundColor.defaultColor
        set(color) = ValueAnimator.ofObject(ArgbEvaluator(), bgColor, color).apply {
            this.addUpdateListener { setCardBackgroundColor(it.animatedValue as Int) }
            this.duration = 200L
        }.start()

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        super.addView(child, params)
        if (child != null && child is TextView) {
            this.tvText = child
        }
    }
}