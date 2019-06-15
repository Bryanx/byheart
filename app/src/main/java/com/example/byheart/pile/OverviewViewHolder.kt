package com.example.byheart.pile

import android.view.View
import android.widget.TextView
import com.example.byheart.R
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder

class OverviewViewHolder(itemView: View) : GroupViewHolder(itemView) {

    private val pileTitle: TextView = itemView.findViewById(R.id.pile_title)

    fun setOverviewTitle(group: ExpandableGroup<*>) {
        pileTitle.text = group.title
    }
}