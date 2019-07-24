package nl.bryanderidder.byheart.pile.edit

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import nl.bryanderidder.byheart.R


/**
 * A color picker custom view which creates a grid of color squares.  The number of squares per
 * row (and the padding between the squares) is determined by the user.
 * @author Bryan de Ridder
 */
class ColorPickerPalette : TableLayout {

    private lateinit var onColorSelectedListener: (color: Int) -> Unit

    private var description1: String? = null
    private var descriptionSelected: String? = null

    private var swatchLength: Int = 0
    private var swatchMargin: Int = 0
    private var amountOfColumns: Int = 0

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context) : super(context) {}

    /**
     * Initialize the size, columns, and listener.  Size should be a pre-defined size (SIZE_LARGE
     * or SIZE_SMALL) from ColorPickerDialogFragment.
     */
    fun init(size: Int, columns: Int, onColorSelect: (color: Int) -> Unit) {
        amountOfColumns = columns
        swatchLength = resources.getDimensionPixelSize(R.dimen.color_swatch_large)
        swatchMargin = resources.getDimensionPixelSize(R.dimen.color_swatch_margins_large)
        onColorSelectedListener = onColorSelect
        description1 = resources.getString(R.string.color_swatch_description)
        descriptionSelected = resources.getString(R.string.color_swatch_description_selected)
    }

    private fun createTableRow(): TableRow {
        val row = TableRow(context)
        val params = ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        row.layoutParams = params
        return row
    }

    /**
     * Add swatches to table in a serpentine format.
     */
    fun drawPalette(colors: IntArray?, selectedColor: Int) {
        if (colors == null) return
        this.removeAllViews()
        var tableElements = 0
        var rowElements = 0
        var rowNumber = 0
        // Fills the table with swatches based on the array of palette.
        var row = createTableRow()
        colors.forEach { color ->
            tableElements++
            val colorSwatch = createColorSwatch(color, selectedColor)
            setSwatchDescription(rowNumber, tableElements, rowElements, color == selectedColor, colorSwatch)
            addSwatchToRow(row, colorSwatch, rowNumber)
            rowElements++
            if (rowElements == amountOfColumns) {
                addView(row)
                row = createTableRow()
                rowElements = 0
                rowNumber++
            }
        }
        // Create blank views to fill the row if the last row has not been filled.
        if (rowElements > 0) {
            while (rowElements != amountOfColumns) {
                addSwatchToRow(row, createBlankSpace(), rowNumber)
                rowElements++
            }
            addView(row)
        }
    }

    /**
     * Appends a swatch to the end of the row for even-numbered rows (starting with row 0),
     * to the beginning of a row for odd-numbered rows.
     */
    private fun addSwatchToRow(row: TableRow, swatch: View, rowNumber: Int) {
        if (rowNumber % 2 == 0) {
            row.addView(swatch)
        } else {
            row.addView(swatch, 0)
        }
    }

    /**
     * Add a content description to the specified swatch view. Because the palette get added in a
     * snaking form, every other row will need to compensate for the fact that the palette are added
     * in an opposite direction from their left->right/top->bottom order, which is how the system
     * will arrange them for accessibility purposes.
     */
    private fun setSwatchDescription(
        rowNumber: Int, index: Int, rowElements: Int, selected: Boolean,
        swatch: View
    ) {
        val accessibilityIndex: Int
        if (rowNumber % 2 == 0) {
            // We're in a regular-ordered row
            accessibilityIndex = index
        } else {
            // We're in a backwards-ordered row.
            val rowMax = (rowNumber + 1) * amountOfColumns
            accessibilityIndex = rowMax - rowElements
        }

        val description: String
        description = when {
            selected -> String.format(descriptionSelected!!, accessibilityIndex)
            else -> String.format(description1!!, accessibilityIndex)
        }
        swatch.contentDescription = description
    }

    private fun createBlankSpace(): ImageView {
        val view = ImageView(context)
        val params = TableRow.LayoutParams(swatchLength, swatchLength)
        params.setMargins(swatchMargin, swatchMargin, swatchMargin, swatchMargin)
        view.layoutParams = params
        return view
    }

    private fun createColorSwatch(color: Int, selectedColor: Int): Swatch {
        val view =
            Swatch(context, color, color == selectedColor, onColorSelectedListener)
        val params = TableRow.LayoutParams(swatchLength, swatchLength)
        params.setMargins(swatchMargin, swatchMargin, swatchMargin, swatchMargin)
        view.layoutParams = params
        return view
    }
}