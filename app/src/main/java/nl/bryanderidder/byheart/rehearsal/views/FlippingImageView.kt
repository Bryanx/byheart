package nl.bryanderidder.byheart.rehearsal.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import nl.bryanderidder.byheart.shared.flipY
import nl.bryanderidder.byheart.shared.onAnimateEnd

class FlippingImageView(context: Context, attrs: AttributeSet) : AppCompatImageView(context, attrs) {

    fun flip() {
        flipY(this, 0f, 90f)
            .onAnimateEnd { flipY(this, -90f, 0f) }
    }
}