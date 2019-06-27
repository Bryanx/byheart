package com.example.byheart.pile

import android.view.View
import android.widget.TextView
import com.example.byheart.MainActivity
import com.example.byheart.R
import com.example.byheart.card.CardFragment
import com.example.byheart.shared.startFragment
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder


class PileViewHolder(itemView: View) : ChildViewHolder(itemView) {

    private val pileName: TextView = itemView.findViewById(R.id.pile_name)
    private val pileId: TextView = itemView.findViewById(R.id.pile_id)

    init {
        itemView.setOnClickListener {
            val activity = itemView.context as MainActivity
            activity.pileId = pileId.text.toString()
            activity.pileName = pileName.text.toString()
            activity.supportFragmentManager.startFragment(CardFragment())
        }
    }

    fun setPileName(pile: Pile) {
        pileName.text = pile.name
    }

    fun setPileId(pile: Pile) {
        pileId.text = pile.id.toString()
    }
}