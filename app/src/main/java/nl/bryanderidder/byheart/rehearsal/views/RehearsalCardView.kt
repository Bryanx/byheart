package nl.bryanderidder.byheart.rehearsal.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.shared.Preferences
import nl.bryanderidder.byheart.shared.getAttr
import nl.bryanderidder.byheart.shared.name

/**
 * Custom TextView for making a simple card.
 * @author Bryan de Ridder
 */
class RehearsalCardView(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs) {

    init {
        changeCameraDistance()
        changeCardColor()
    }

    private fun changeCardColor() {
        val darkMode = Preferences.DARK_MODE
        when {
            this.name == "cardBack" && darkMode -> setBgAndTxt(R.drawable.shadow_dark_back, Color.WHITE)
            this.name != "cardBack" && darkMode -> setBgAndTxt(R.drawable.shadow_dark)
            this.name == "cardBack" && !darkMode -> setBgAndTxt(R.drawable.shadow_back, Color.WHITE)
            else -> setBgAndTxt(R.drawable.shadow)
        }
    }

    private fun setBgAndTxt(id: Int, color: Int = context.getAttr(R.attr.mainTextColor)) {
        this.setBackgroundResource(id)
        this.setTextColor(color)
    }

    private fun changeCameraDistance() {
        val distance = 8000
        val scale = resources.displayMetrics.density * distance
        this.cameraDistance = scale
    }

    //Make square based on width
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

}