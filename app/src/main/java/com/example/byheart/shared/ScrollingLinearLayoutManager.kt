package com.example.byheart.shared

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

/**
 * LinearLayoutManager that contains a check on vertical scroll.
 * This is temporary solution because Android has no simple solution to prevent scrolling when
 * all items are visible.
 * @author Bryan de Ridder
 */
class ScrollingLinearLayoutManager(context: Context) : LinearLayoutManager(context) {

    override fun canScrollVertically(): Boolean = when {
        this.childCount <= 3 -> false
        else -> super.canScrollVertically()
    }
}