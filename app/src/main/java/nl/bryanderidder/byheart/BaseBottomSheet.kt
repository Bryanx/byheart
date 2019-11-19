package nl.bryanderidder.byheart

import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Base class for bottom sheet fragment.
 * Status bar color is adjusted to dimmed background.
 * Standard theme is given with rounded corners.
 * @author Bryan de Ridder
 */
abstract class BaseBottomSheet : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }
    override fun getTheme() = R.style.AppBottomSheetDialogTheme
}