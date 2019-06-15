package com.example.byheart.shared

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.byheart.R
import com.example.byheart.pile.OverviewViewHolder
import com.example.byheart.pile.Pile
import com.example.byheart.pile.PileViewHolder
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup


class PileAdapter(groups: List<ExpandableGroup<*>>) : ExpandableRecyclerViewAdapter<OverviewViewHolder, PileViewHolder>(groups) {

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): OverviewViewHolder {
        val inflater = parent.context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.list_group, parent, false)
        view.isClickable = true
        view.addRipple()
        return OverviewViewHolder(view)
    }

    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): PileViewHolder {
        val inflater = parent.context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.list_item, parent, false)
        view.isClickable = true
        view.addRipple()
        return PileViewHolder(view)
    }

    override fun onBindChildViewHolder(holder: PileViewHolder, flatPosition: Int, group: ExpandableGroup<*>, childIndex: Int) {
        val pile = group.items[childIndex] as Pile
        holder.setPileName(pile)
    }

    override fun onBindGroupViewHolder(holder: OverviewViewHolder, flatPosition: Int, group: ExpandableGroup<*>) {
        holder.setOverviewTitle(group)
    }

    private fun View.addRipple() = with(TypedValue()) {
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, this, true)
        setBackgroundResource(resourceId)
    }
}