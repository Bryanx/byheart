package nl.bryanderidder.byheart.rehearsal.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.shared.Preferences
import nl.bryanderidder.byheart.shared.getAttr
import nl.bryanderidder.byheart.shared.name


class RehearsalCardView : AppCompatTextView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        changeCameraDistance()
        changeCardColor()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    private fun changeCardColor() {
        val darkMode = Preferences.DARK_MODE
        when {
            this.name == "cardBack" && darkMode -> setBgAndTxt(R.drawable.shadow_dark_back, Color.WHITE)
            this.name != "cardBack" && darkMode -> setBgAndTxt(R.drawable.shadow_dark, Color.WHITE)
            this.name == "cardBack" && !darkMode -> setBgAndTxt(R.drawable.shadow_back, Color.WHITE)
            else -> setBgAndTxt(R.drawable.shadow, context.getAttr(R.attr.mainHeaderTextColor))
        }
    }

    private fun setBgAndTxt(id: Int, color: Int) {
        this.setBackgroundResource(id)
        this.setTextColor(color)
    }

    private fun changeCameraDistance() {
        val distance = 8000
        val scale = resources.displayMetrics.density * distance
        this.cameraDistance = scale
    }

}