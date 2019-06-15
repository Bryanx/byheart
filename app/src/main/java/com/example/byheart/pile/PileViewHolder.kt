package com.example.byheart.pile

import android.view.View
import android.widget.TextView
import com.example.byheart.R
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder

class PileViewHolder(itemView: View) : ChildViewHolder(itemView) {

    private val pileName: TextView = itemView.findViewById(R.id.pile_name)

    fun setPileName(pile: Pile) {
        pileName.text = pile.name
    }
}