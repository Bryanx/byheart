package nl.bryanderidder.byheart.pile.edit

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatDialogFragment
import nl.bryanderidder.byheart.R


/**
 * A dialog which takes in as input an array of palette and creates a palette allowing the user to
 * select a specific color swatch, which invokes a listener.
 * @author Bryan de Ridder
 */
class ColorPickerDialog : AppCompatDialogFragment() {

    protected var title = "Pick a color"
    protected var mColors: IntArray = intArrayOf(
        R.color.red_200, R.color.deep_orange_200, R.color.orange_200, R.color.yellow_200,
        R.color.lime_200, R.color.green_A200, R.color.teal_A200, R.color.cyan_A200,
        R.color.blue_200, R.color.indigo_200, R.color.deep_purple_200, R.color.purple_200
    )
    var mSelectedColor: Int = 0
    protected var mColumns: Int = 0
    protected var mSize: Int = 0

    internal lateinit var palette: ColorPickerPalette

    protected lateinit var listener: (color: Int) -> Unit

    var colors: IntArray
        get() = mColors
        set(colors) {
            if (mColors != colors) {
                mColors = colors
                refreshPalette()
            }
        }

    var selectedColor: Int
        get() = mSelectedColor
        set(color) {
            if (mSelectedColor != color) {
                mSelectedColor = color
                refreshPalette()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            title = "Select card color"
            mColumns = arguments!!.getInt(KEY_COLUMNS)
            mSize = arguments!!.getInt(KEY_SIZE)
        }
        if (savedInstanceState != null) {
            mSelectedColor = (savedInstanceState.getSerializable(KEY_SELECTED_COLOR) as Int?)!!
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(activity).inflate(R.layout.color_picker_dialog, null)
        palette = view.findViewById(R.id.color_picker)
        palette.init(mSize, mColumns, this::onSelectColor)
        if (mColors != null) showPaletteView()
        return AlertDialog.Builder(activity)
            .setView(view)
            .create()
    }

    fun initialize(titleResId: Int, colors: IntArray? = null, selectedColor: Int, columns: Int, size: Int) {
        setArguments(titleResId, columns, size)
        setColors(colors, selectedColor)
    }

    fun setOnColorSelectedListener(listener: (color: Int) -> Unit) {
        this.listener = listener
    }

    private fun showPaletteView() {
        refreshPalette()
        palette.visibility = View.VISIBLE
    }

    fun setColors(colors: IntArray?, selectedColor: Int) {
        mSelectedColor = selectedColor
    }

    private fun onSelectColor(color: Int) {
        if (color != mSelectedColor) {
            listener.invoke(color)
            mSelectedColor = color
            refreshPalette()
        }
        dismiss()
    }

    private fun refreshPalette() {
        palette.drawPalette(mColors, mSelectedColor)
    }

    private fun setArguments(titleResId: Int, columns: Int, size: Int) {
        val bundle = Bundle()
        bundle.putInt(KEY_TITLE_ID, titleResId)
        bundle.putInt(KEY_COLUMNS, columns)
        bundle.putInt(KEY_SIZE, size)
        arguments = bundle
    }

    companion object {
        val SIZE_LARGE = 1
        val SIZE_SMALL = 2
        protected const val KEY_TITLE_ID = "title_id"
        protected const val KEY_COLORS = "palette"
        protected const val KEY_SELECTED_COLOR = "selected_color"
        protected const val KEY_COLUMNS = "columns"
        protected const val KEY_SIZE = "size"

        fun newInstance(
            titleResId: Int, colors: IntArray?, selectedColor: Int,
            columns: Int, size: Int
        ): ColorPickerDialog {
            val ret = ColorPickerDialog()
            ret.initialize(titleResId, null, selectedColor, columns, size)
            return ret
        }
    }
}