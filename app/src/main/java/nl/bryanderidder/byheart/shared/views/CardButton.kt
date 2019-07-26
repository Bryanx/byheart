package nl.bryanderidder.byheart.shared.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.shared.getAttr
import nl.bryanderidder.byheart.shared.string

class CardButton(ctx: Context, attrs: AttributeSet) : RelativeLayout(ctx, attrs) {

    private var defaultTextColor: Int = context.getAttr(R.attr.mainTextColor)
    private val defaultBg: Int = context.getAttr(R.attr.mainBackgroundColorLighter)
    private var tvText: TextView
    private var cbCardView: CardView

    var textColor: Int
        get() = tvText.currentTextColor
        set(textColor) = tvText.setTextColor(textColor)

    var text: String
        get() = tvText.string
        set(text) = tvText.setText(text)

    init {
        LayoutInflater.from(context).inflate(R.layout.card_button, this)
        tvText = findViewById(R.id.cbText)
        cbCardView = findViewById(R.id.cbCardView)
        handleStyledAttributes(attrs)
    }

    private fun handleStyledAttributes(attrs: AttributeSet) {
        val styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.CardButton)
        setCardBackgroundColor(styledAttrs.getInt(R.styleable.CardButton_backgroundColor, defaultBg))
        this.textColor = styledAttrs.getInt(R.styleable.CardButton_textColor, defaultTextColor)
        val text = styledAttrs.getString(R.styleable.CardButton_text)
        text?.let { this.text = it }
        styledAttrs.recycle()
    }

    fun setCardBackgroundColor(color: Int) = cbCardView.setCardBackgroundColor(color)
}