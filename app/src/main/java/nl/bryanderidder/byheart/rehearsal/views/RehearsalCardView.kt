package nl.bryanderidder.byheart.rehearsal.views

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.shared.getAttr
import nl.bryanderidder.byheart.shared.utils.px

/**
 * Custom TextView for making a simple card.
 * @author Bryan de Ridder
 */
class RehearsalCardView(context: Context, attrs: AttributeSet) : CardView(context, attrs) {

    lateinit var textView: TextView

    var bgColor: Int
        get() = cardBackgroundColor.defaultColor
        set(color) = setCardBackgroundColor(color)

    init {
        radius = 15.px.toFloat()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) elevation = 0F
        changeCameraDistance()
        bgColor = context.getAttr(R.attr.mainBackgroundColorLight)
    }

    private fun changeCameraDistance() {
        val distance = 8000
        val scale = resources.displayMetrics.density * distance
        this.cameraDistance = scale
    }

    //Make square based on width
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) =
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        super.addView(child, params)
        if (child != null && child is TextView) {
            this.textView = child
            this.textView.setTextColor(context.getAttr(R.attr.mainTextColor))
        }
    }

}