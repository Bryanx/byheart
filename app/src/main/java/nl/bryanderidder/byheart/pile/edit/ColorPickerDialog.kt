package nl.bryanderidder.byheart.pile.edit

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.shared.utils.getColors
import java.io.Serializable

/**
 * A dialog which takes in as input an array of palette and creates a palette allowing the user to
 * select a specific color swatch, which invokes a listener.
 * @author Bryan de Ridder
 */
class ColorPickerDialog : AppCompatDialogFragment() {

    protected var title = "Pick a color"
    protected lateinit var mColors: IntArray
    var mSelectedColor: Int = 0
    protected var mColumns: Int = 0
    protected var mSize: Int = 0
    internal lateinit var palette: ColorPickerPalette
    lateinit var listener: (color: Int) -> Unit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return super.onCreateView(inflater, container, savedInstanceState)
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
            listener = (savedInstanceState.getSerializable(KEY_LISTENER) as (Int) -> Unit)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(activity).inflate(R.layout.color_picker_dialog, null)
        palette = view.findViewById(R.id.color_picker)
        palette.init(mSize, mColumns, this::onSelectColor)
        mColors = getColors(context!!)
        showPaletteView()
        return AlertDialog.Builder(activity)
            .setView(view)
            .create()
    }

    fun initialize(titleResId: Int, colors: IntArray? = null, selectedColor: Int, columns: Int, size: Int) {
        setArguments(titleResId, columns, size)
        mSelectedColor = selectedColor
    }

    private fun showPaletteView() {
        refreshPalette()
        palette.visibility = View.VISIBLE
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
        arguments = Bundle().apply {
            putInt(KEY_TITLE_ID, titleResId)
            putInt(KEY_COLUMNS, columns)
            putInt(KEY_SIZE, size)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntArray(KEY_COLORS, mColors)
        outState.putSerializable(KEY_SELECTED_COLOR, mSelectedColor)
        outState.putSerializable(KEY_LISTENER, listener as Serializable)
    }

    companion object {
        protected const val KEY_TITLE_ID = "title_id"
        protected const val KEY_COLORS = "palette"
        protected const val KEY_SELECTED_COLOR = "selected_color"
        protected const val KEY_COLUMNS = "columns"
        protected const val KEY_SIZE = "size"
        private const val KEY_LISTENER = "listener"

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