package nl.bryanderidder.byheart.rehearsal.views

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import nl.bryanderidder.byheart.shared.onAnimateEnd
import nl.bryanderidder.byheart.shared.pronounce
import nl.bryanderidder.byheart.shared.utils.flipY
import java.util.*

class FlippingImageView(context: Context, attrs: AttributeSet) : AppCompatImageView(context, attrs) {

    private lateinit var textToSpeech: TextToSpeech

    var language: Locale
        get() = textToSpeech.language
        set(lang) {
            textToSpeech.language = lang
        }

    init {
        textToSpeech = TextToSpeech(context) {
            if (it == TextToSpeech.SUCCESS) {
                textToSpeech.language = Locale.ENGLISH
            }
        }
    }

    fun flip(duration: Long) {
        flipY(this, 0f, 90f, duration)
            .onAnimateEnd { flipY(this, -90f, 0f, duration) }
    }

    fun pronounce(text: String) = textToSpeech.pronounce(text)

    override fun onDetachedFromWindow() {
        textToSpeech.shutdown()
        super.onDetachedFromWindow()
    }
}