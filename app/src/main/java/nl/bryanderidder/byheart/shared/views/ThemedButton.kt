package nl.bryanderidder.byheart.shared.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.animation.doOnStart
import kotlinx.android.synthetic.main.view_themedbutton.view.*
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.shared.getAttr
import nl.bryanderidder.byheart.shared.setTintColor
import nl.bryanderidder.byheart.shared.string
import nl.bryanderidder.byheart.shared.utils.px


/**
 * Custom button with rounded corners.
 * @author Bryan de Ridder
 */
class ThemedButton(ctx: Context, attrs: AttributeSet) : RelativeLayout(ctx, attrs) {

    private val defaultTextColor: Int = context.getAttr(R.attr.mainTextColor)
    private val defaultBg: Int = context.getAttr(R.attr.mainBackgroundColorLighter)
    private val defaultCornerRadius: Int = 22
    private var tvText: TextView
    private var cardView: CardView
    private var ivIcon: ImageView

    var textColor: Int
        get() = tvText.currentTextColor
        set(textColor) = tvText.setTextColor(textColor)

    var text: String
        get() = tvText.string
        set(text) {
            tvText.text = text
        }

    var cornerRadius: Int
        get() = cardView.radius.toInt()
        set(cornerRadius) {
            cardView.radius = cornerRadius.px.toFloat()
        }

    var btnHeight: Int
        get() = cardView.height
        set(btnHeight) {
            cardView.layoutParams.height = btnHeight
        }

    var btnWidth: Int
        get() = cardView.width
        set(btnWidth) {
            cardView.layoutParams.width = btnWidth
            cbText.layoutParams.width = btnWidth
        }

    var paddingHorizontal: Int
        get() = cbText.paddingStart
        set(paddingHorizontal) = cbText.setPadding(
            paddingHorizontal.px,
            cbText.paddingTop,
            paddingHorizontal.px,
            cbText.paddingBottom
        )

    var btnBackgroundColor: Int
        get() = cardView.cardBackgroundColor.defaultColor
        set(btnBackgroundColor) {
            cardView.setCardBackgroundColor(btnBackgroundColor)
        }

    var icon: Drawable
        get() = ivIcon.background
        set(icon) {
            ivIcon.setImageDrawable(icon)
            ivIcon.visibility = VISIBLE
        }

    var iconColor: Int
        get() = ivIcon.solidColor
        set(iconColor) = ivIcon.setTintColor(iconColor)

    init {
        LayoutInflater.from(context).inflate(R.layout.view_themedbutton, this)
        tvText = findViewById(R.id.cbText)
        cardView = findViewById(R.id.cbCardView)
        ivIcon = findViewById(R.id.cbIcon)
        handleStyledAttributes(attrs)
    }

    private fun handleStyledAttributes(attrs: AttributeSet) {
        val styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.ThemedButton)
        this.btnBackgroundColor = styledAttrs.getInt(R.styleable.ThemedButton_backgroundColor, defaultBg)
        this.textColor = styledAttrs.getInt(R.styleable.ThemedButton_textColor, defaultTextColor)
        this.cornerRadius = styledAttrs.getInt(R.styleable.ThemedButton_btnCornerRadius, defaultCornerRadius)
        this.iconColor = styledAttrs.getInt(R.styleable.ThemedButton_iconColor, defaultTextColor)
        styledAttrs.getString(R.styleable.ThemedButton_text)?.let { text = it }
        styledAttrs.getDrawable(R.styleable.ThemedButton_icon)?.let { icon = it }
        styledAttrs.recycle()
    }

    fun animateBg(bgColor: Int, x: Float, y: Float) = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
            val reveal = ViewAnimationUtils.createCircularReveal(
                this,
                x.toInt(),
                y.toInt(),
                0.toFloat(),
                (cardView.height).toFloat()
            )
            reveal.interpolator = AccelerateDecelerateInterpolator()
            reveal.duration = 400
            reveal.doOnStart { cardView.setCardBackgroundColor(bgColor) }
            reveal.start()
        }
        else -> cardView.setCardBackgroundColor(bgColor)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}