package nl.bryanderidder.byheart.shared.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.shared.color
import nl.bryanderidder.byheart.shared.getAttr
import nl.bryanderidder.byheart.shared.utils.px

/**
 * Group of buttons, only allowed to select one at a time.
 * @author Bryan de Ridder
 */
class ThemedButtonGroup(private var ctx: Context, attrs: AttributeSet) : LinearLayout(ctx, attrs) {

    private var btns = mutableListOf<ThemedButton>()
    var highlightBgColor: Int = ctx.color(R.color.colorPrimary)
    var highLightText: Int = ctx.color(R.color.white)
    var normalBgColor: Int = ctx.getAttr(R.attr.dialogBackgroundButton)
    var normalText: Int = context.getAttr(R.attr.mainTextColor)

    private fun addListener(btn: ThemedButton) {
        btn.setOnTouchListener { _, event ->
            if (!btn.isSelected) btn.animateBg(highlightBgColor, event.x, event.y)
            styleSelected(btn)
            btns.filter { it != btn }.forEach { styleDeselected(it) }
            if (event.action == MotionEvent.ACTION_DOWN) btn.performClick()
            event.action == MotionEvent.ACTION_DOWN
        }
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        super.addView(child, params)
        if (child != null && child is ThemedButton) {
            child.cornerRadius = 10
            child.btnHeight = 155.px
            child.btnWidth = ViewGroup.LayoutParams.MATCH_PARENT
            child.paddingHorizontal = 4.px
            btns.add(child)
            addListener(child)
        }
    }

    private fun styleDeselected(btn: ThemedButton) {
        btn.isSelected = false
        btn.textColor = normalText
        btn.iconColor = normalText
        btn.btnBackgroundColor = normalBgColor
    }

    private fun styleSelected(btn: ThemedButton) {
        btn.isSelected = true
        btn.textColor = highLightText
        btn.iconColor = highLightText
        btn.btnBackgroundColor = highlightBgColor
    }

    fun styleButtons() = btns.filter { it.isSelected }.forEach { styleSelected(it) }
}