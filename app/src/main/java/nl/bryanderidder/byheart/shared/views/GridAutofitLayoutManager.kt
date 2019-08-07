package nl.bryanderidder.byheart.shared.views

import android.content.Context
import android.util.TypedValue
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max

/**
 * GridLayoutManager that automatically fits on all screen sizes.
 * The amount of columns is based on the amount of screen space.
 * @author Bryan de Ridder
 */
class GridAutofitLayoutManager/* Initially set spanCount to 1, will be changed automatically later. */(
    context: Context,
    columnWidth: Int
) : GridLayoutManager(context, 1) {
    private var columnWidth: Int = 0
    private var isColumnWidthChanged = true
    private var lastWidth: Int = 0
    private var lastHeight: Int = 0

    init {
        setColumnWidth(checkedColumnWidth(context, columnWidth))
    }

    private fun checkedColumnWidth(context: Context, columnWidth: Int): Int {
        var columnWidth = columnWidth
        if (columnWidth <= 0) {
            /* Set default columnWidth value (48dp here). It is better to move this constant
            to static constant on top, but we need context to convert it to dp, so can't really
            do so. */
            columnWidth = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 48f,
                context.resources.displayMetrics
            ).toInt()
        }
        return columnWidth
    }

    private fun setColumnWidth(newColumnWidth: Int) {
        if (newColumnWidth > 0 && newColumnWidth != columnWidth) {
            columnWidth = newColumnWidth
            isColumnWidthChanged = true
        }
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        val width = width
        val height = height
        if (columnWidth > 0 && width > 0 && height > 0 && (isColumnWidthChanged || lastWidth != width || lastHeight != height)) {
            val totalSpace: Int = if (orientation == LinearLayoutManager.VERTICAL) {
                width - paddingRight - paddingLeft
            } else {
                height - paddingTop - paddingBottom
            }
            val spanCount = max(1, totalSpace / columnWidth)
            setSpanCount(spanCount)
            isColumnWidthChanged = false
        }
        lastWidth = width
        lastHeight = height
        super.onLayoutChildren(recycler, state)
    }
}