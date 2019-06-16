package com.example.byheart.pile

import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.byheart.R
import com.example.byheart.card.CardFragment
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder


class PileViewHolder(itemView: View) : ChildViewHolder(itemView) {

    private val pileName: TextView = itemView.findViewById(R.id.pile_name)
    private val pileId: TextView = itemView.findViewById(R.id.pile_id)

    init {
        itemView.setOnClickListener {
            (itemView.context as AppCompatActivity).supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.main_container,
                    CardFragment.newInstance(
                        pileName.text.toString(),
                        pileId.text.toString()
                    )
                )
                .commit()
        }
    }

    fun setPileName(pile: Pile) {
        pileName.text = pile.name
    }

    fun setPileId(pile: Pile) {
        pileId.text = pile.id.toString()
    }
}