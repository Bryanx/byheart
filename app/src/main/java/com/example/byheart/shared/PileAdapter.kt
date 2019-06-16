package com.example.byheart.shared

import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import com.example.byheart.R
import com.example.byheart.pile.OverviewViewHolder
import com.example.byheart.pile.Pile
import com.example.byheart.pile.PileViewHolder
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup

class PileAdapter(groups: List<ExpandableGroup<*>>) : ExpandableRecyclerViewAdapter<OverviewViewHolder, PileViewHolder>(groups) {

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): OverviewViewHolder {
        val view = parent.inflate(R.layout.list_group)
        return OverviewViewHolder(view)
    }

    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): PileViewHolder {
        val view = parent.inflate(R.layout.list_item)
        return PileViewHolder(view)
    }

    override fun onBindChildViewHolder(holder: PileViewHolder, flatPosition: Int, group: ExpandableGroup<*>, childIndex: Int) {
        val pile = group.items[childIndex] as Pile
        holder.setPileName(pile)
        holder.setPileId(pile)
    }

    override fun onBindGroupViewHolder(holder: OverviewViewHolder, flatPosition: Int, group: ExpandableGroup<*>) {
        holder.setOverviewTitle(group)
    }
}