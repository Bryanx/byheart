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
import nl.bryanderidder.byheart.shared.Preferences
import nl.bryanderidder.byheart.shared.setTint
import nl.bryanderidder.byheart.shared.string
import java.util.*

/**
 * Simple RelativeLayout that animates upon layout changes.
 * Should have android:animateLayoutChanges="true" as an attribute in xml.
 * @author Bryan de Ridder
 */
class RehearsalCard(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {

    private lateinit var languageCardBack: Locale
    private lateinit var languageCardFront: Locale
    private lateinit var flipOut: AnimatorSet
    private lateinit var flipIn: AnimatorSet
    var cardFront: RehearsalCardView
    var cardBack: RehearsalCardView
    private var ivPronounce: FlippingImageView
    var backIsVisible = false

    var frontText: String?
        get() = cardFront.string
        set(text) {
            cardFront.text = text
        }

    var backText: String?
        get() = cardBack.string
        set(text) {
            cardBack.text = text
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
            handler.postDelayed({ ivPronounce.setTint(R.color.colorPrimaryDark) }, 100)
            flipIn.setTarget(cardFront)
            flipOut.setTarget(cardBack)
            flipIn.start()
            flipOut.start()
            true
        } else {
            handler.postDelayed({ ivPronounce.setTint(R.color.grey_500) }, 150)
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
        handler.postDelayed({ ivPronounce.setTint(R.color.grey_500) }, 100)
    }

    fun addPronounceLocale(frontLocale: Locale, backLocale: Locale) {
        languageCardFront = frontLocale
        languageCardBack = backLocale
        ivPronounce.setOnClickListener {
            if (backIsVisible) speakCard(cardBack, backLocale)
            else speakCard(cardFront, frontLocale)
        }
    }

    private fun speakCard(tv: TextView, language: Locale) {
        when {
            !Preferences.REHEARSAL_REVERSE -> ivPronounce.language = language
            tv == cardBack -> ivPronounce.language = languageCardFront
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

    fun sayBackCard() = speakCard(cardBack, languageCardBack)
}