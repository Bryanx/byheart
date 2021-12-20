package nl.bryanderidder.byheart.shared.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity.CENTER_VERTICAL
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.android.material.switchmaterial.SwitchMaterial
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.R.styleable.*
import nl.bryanderidder.byheart.shared.*
import nl.bryanderidder.byheart.shared.utils.px


/**
 * Custom switch with preconfigured colors
 * @author Bryan de Ridder
 */
class CustomSwitch(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private var switch: SwitchMaterial = SwitchMaterial(context)

    private var ivIcon: ImageView = ImageView(context)

    var icon: Drawable
        get() = ivIcon.background
        set(icon) {
            ivIcon.setImageDrawable(icon)
            val iconLayoutParams = LayoutParams(30.px, 30.px)
            iconLayoutParams.gravity = CENTER_VERTICAL
            iconLayoutParams.marginEnd = 16.px
            ivIcon.layoutParams = iconLayoutParams
        }

    var text: String
        get() = switch.text.toString()
        set(text) {
            switch.text = text
        }

    var textColor: Int
        get() = switch.textColors.defaultColor
        set(value) {
            switch.setTextColor(value)
        }

    var iconColor: Int
        get() = ivIcon.solidColor
        set(value) {
            ivIcon.setTintColor(value)
        }

    var switchColor: Int = -7288071
        set(value) {
            field = value
            updateColors(switch.isChecked)
        }

    var isChecked: Boolean = switch.isChecked
        set(value) {
            switch.isChecked = value
            field = value
        }

    init {
        val iconLayoutParams = LayoutParams(30.px, 30.px)
        ivIcon.setTint(context.getAttr(R.attr.mainTextColor))
        addView(ivIcon, iconLayoutParams)

        val switchLayoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        switchLayoutParams.gravity = CENTER_VERTICAL
        addView(switch, switchLayoutParams)

        getStyledAttributes(attrs)
    }

    private fun getStyledAttributes(attrs: AttributeSet) {
        val attrs = context.obtainStyledAttributes(attrs, CustomSwitch)
        attrs.getDrawable(CustomSwitch_icon)?.let { this.icon = it }
        this.textColor = attrs.getColor(CustomSwitch_textColor, context.getAttr(R.attr.mainTextColor))
        this.text = attrs.getString(CustomSwitch_text) ?: this.text
        attrs.recycle()
    }

    fun setOnCheckedChanged(callback: (checked: Boolean) -> Unit) {
        switch.setOnCheckedChangeListener { _, checked ->
            callback(checked)
            updateColors(checked)
        }
    }

    private fun updateColors(checked: Boolean) {
        if (Preferences.DARK_MODE) {
            textColor = if (checked) switchColor else context!!.color(R.color.white_ac)
            iconColor = if (checked) switchColor else context!!.color(R.color.white_ac)
            switch.checkedColor = switchColor.setBrightness(0.70F)
            switch.unCheckedColor = context!!.color(R.color.white_ac)
        } else {
            textColor = if (checked) switchColor.setBrightness(0.55F) else context!!.color(R.color.grey_700)
            iconColor = if (checked) switchColor.setBrightness(0.65F) else context!!.color(R.color.grey_700)
            switch.checkedColor = switchColor
            switch.unCheckedColor = context!!.color(R.color.grey_400)
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        switch.isEnabled = enabled
    }
}