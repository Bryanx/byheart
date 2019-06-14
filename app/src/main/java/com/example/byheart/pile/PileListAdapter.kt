package com.example.byheart.pile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.byheart.R

class PileListAdapter internal constructor(context: Context) : RecyclerView.Adapter<PileListAdapter.PileViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var piles = emptyList<Pile>() // Cached copy

    inner class PileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pileItemView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PileViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_card_item, parent, false)
        return PileViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PileViewHolder, position: Int) {
        val current = piles[position]
        holder.pileItemView.text = current.name
    }

    internal fun setPiles(piles: List<Pile>) {
        this.piles = piles
        notifyDataSetChanged()
    }

    override fun getItemCount() = piles.size
}
