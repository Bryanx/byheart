package nl.bryanderidder.byheart.rehearsal.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import nl.bryanderidder.byheart.shared.flipY
import nl.bryanderidder.byheart.shared.onAnimateEnd

class FlippingImageView(context: Context, attrs: AttributeSet) : AppCompatImageView(context, attrs) {

    fun flip(duration: Long) {
        flipY(this, 0f, 90f, duration)
            .onAnimateEnd { flipY(this, -90f, 0f, duration) }
    }
}