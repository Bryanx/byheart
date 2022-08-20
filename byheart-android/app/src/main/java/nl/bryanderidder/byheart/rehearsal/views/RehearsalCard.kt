package nl.bryanderidder.byheart.rehearsal.views

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import android.widget.TextView
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.shared.*
import java.util.*

/**
 * Simple RelativeLayout that animates upon layout changes.
 * Should have android:animateLayoutChanges="true" as an attribute in xml.
 * @author Bryan de Ridder
 */
class RehearsalCard(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {

    private var backColor: Int = R.color.colorPrimaryDark
    private lateinit var languageCardBack: Locale
    private lateinit var languageCardFront: Locale
    private lateinit var flipOut: AnimatorSet
    private lateinit var flipIn: AnimatorSet
    private var cardFront: RehearsalCardView
    private var cardBack: RehearsalCardView
    private var ivPronounce: FlippingImageView
    private var backIsVisible = false

    var frontText: String?
        get() = cardFront.textView.string
        set(text) {
            cardFront.textView.text = text
        }

    var backText: String
        get() = cardBack.textView.string
        set(text) {
            cardBack.textView.text = text
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_rehearsal_card, this)
        this.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        cardFront = findViewById(R.id.rcCardFront)
        cardBack = findViewById(R.id.rcCardBack)
        ivPronounce = findViewById(R.id.rcIvPronounce)
        loadAnimations()
    }

    private fun loadAnimations() {
        flipIn = AnimatorInflater.loadAnimator(context, R.animator.animate_out_flip) as AnimatorSet
        flipOut = AnimatorInflater.loadAnimator(context, R.animator.animate_in_flip) as AnimatorSet
    }

    fun flipCard() {
        ivPronounce.flip(150L)
        backIsVisible = if (!backIsVisible) {
            handler.postDelayed({ ivPronounce.setTint(backColor.setBrightness(0.2F)) }, 100)
            flipIn.setTarget(cardFront)
            flipOut.setTarget(cardBack)
            flipIn.start()
            flipOut.start()
            true
        } else {
            handler.postDelayed({ ivPronounce.setTint(context.color(R.color.grey_500)) }, 150)
            flipIn.setTarget(cardBack)
            flipOut.setTarget(cardFront)
            flipIn.start()
            flipOut.start()
            false
        }
    }

    fun resetCard() {
        cardFront.alpha = 1F
        cardFront.rotationY = 0F
        backIsVisible = false
        cardBack.alpha = 0F
        handler.postDelayed({ ivPronounce.setTint(context.color(R.color.grey_500)) }, 100)
    }

    fun addPronounceLocale(frontLocale: Locale?, backLocale: Locale?) {
        languageCardFront = frontLocale ?: Locale.getDefault()
        languageCardBack = backLocale ?: Locale.getDefault()
        ivPronounce.setOnClickListener {
            if (backIsVisible) speakCard(cardBack.textView, languageCardFront)
            else speakCard(cardFront.textView, languageCardBack)
        }
    }

    private fun speakCard(tv: TextView, language: Locale) {
        when {
            !Preferences.REHEARSAL_REVERSE -> ivPronounce.language = language
            tv == cardBack.textView -> ivPronounce.language = languageCardFront
            else -> ivPronounce.language = languageCardBack
        }
        ivPronounce.pronounce(tv.string)
    }

    fun turnToFront() {
        if (backIsVisible) flipCard()
    }

    fun turnToBack() {
        if (!backIsVisible) flipCard()
    }

    fun sayBackCard() = speakCard(cardBack.textView, languageCardBack)

    fun setBackColor(color: Int) {
        this.backColor = color
        if (Preferences.DARK_MODE) cardFront.textView.setTextColor(color)
        else cardFront.textView.setTextColor(color.setBrightness(0.55F))
        if (Preferences.DARK_MODE) cardBack.bgColor = color.setBrightness(0.65F)
        else cardBack.bgColor = color
        cardBack.textView.setTextColor(context.color(R.color.white))
    }
}