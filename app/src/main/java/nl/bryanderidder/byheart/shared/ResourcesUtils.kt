package nl.bryanderidder.byheart.shared

import android.content.Context
import nl.bryanderidder.byheart.R

object ResourcesUtils {
    fun getColors(ctx: Context): IntArray = ctx.resources.getIntArray(R.array.swatch_colors)
}
