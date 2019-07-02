package com.example.byheart.shared

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ScrollingLinearLayoutManager(context: Context) : LinearLayoutManager(context) {

    override fun canScrollVertically(): Boolean = when {
        this.childCount <= 3 -> false
        else -> super.canScrollVertically()
    }
}