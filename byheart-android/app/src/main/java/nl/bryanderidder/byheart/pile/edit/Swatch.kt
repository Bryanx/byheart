package nl.bryanderidder.byheart.pile.edit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.shared.setDrawableColor

/**
 * Custom view for a swatch, containing a top image and checkmark above.
 * @author Bryan de Ridder
 */
class Swatch(
    context: Context, private val mColor: Int, checked: Boolean,
    private val onColorSelectedListener: (color: Int) -> Unit
)  : FrameLayout(context), View.OnClickListener {

    private val swatchImage: ImageView
    private val checkmarkImage: ImageView

    init {
        LayoutInflater.from(context).inflate(R.layout.view_swatch, this)
        swatchImage = findViewById<View>(R.id.color_picker_swatch) as ImageView
        checkmarkImage = findViewById<View>(R.id.color_picker_checkmark) as ImageView
        setColor(mColor)
        setChecked(checked)
        setOnClickListener(this)
    }

    private fun setChecked(checked: Boolean) = when {
        checked -> checkmarkImage.visibility = View.VISIBLE
        else -> checkmarkImage.visibility = View.GONE
    }

    private fun setColor(color: Int) = swatchImage.setDrawableColor(R.drawable.ic_circle, color)

    override fun onClick(v: View) = onColorSelectedListener.invoke(mColor)
}