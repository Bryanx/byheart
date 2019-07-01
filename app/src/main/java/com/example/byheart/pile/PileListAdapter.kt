package com.example.byheart.pile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.byheart.MainActivity
import com.example.byheart.R
import com.example.byheart.card.CardFragment
import com.example.byheart.shared.startFragment

class PileListAdapter internal constructor(context: Context) : RecyclerView.Adapter<PileListAdapter.PileViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var piles = emptyList<Pile>() // Cached copy

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PileViewHolder {
        val itemView = inflater.inflate(R.layout.item_pile, parent, false)
        return PileViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PileViewHolder, position: Int) {
        val current = piles[position]
        holder.tvFront.text = current.name
        holder.tvPileId.text = current.id.toString()
    }

    internal fun setPiles(piles: List<Pile>) {
        this.piles = piles
        notifyDataSetChanged()
    }

    override fun getItemCount() = piles.size

    inner class PileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvFront: TextView = itemView.findViewById(R.id.tvPileFront)
        val tvPileId: TextView = itemView.findViewById(R.id.tvPileId)
        init {
            itemView.setOnClickListener {
                val activity = itemView.context as MainActivity
                activity.pileId = tvPileId.text.toString()
                activity.pileName = tvFront.text.toString()
                activity.supportFragmentManager.startFragment(CardFragment())
            }
        }
    }
}
