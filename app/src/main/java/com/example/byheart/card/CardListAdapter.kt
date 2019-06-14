package com.example.byheart.card

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.byheart.R

class CardListAdapter internal constructor(context: Context) : RecyclerView.Adapter<CardListAdapter.CardViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var cards = emptyList<Card>() // Cached copy

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardItemView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_card_item, parent, false)
        return CardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val current = cards[position]
        holder.cardItemView.text = current.question
        holder.cardItemView.setOnClickListener {
            if (holder.cardItemView.text == current.question) {
                holder.cardItemView.text = current.answer
                holder.cardItemView.setTextColor(Color.WHITE)
                holder.cardItemView.setBackgroundResource(R.drawable.card_answer)
            } else {
                holder.cardItemView.text = current.question
                holder.cardItemView.setTextColor(Color.BLACK)
                holder.cardItemView.setBackgroundResource(R.drawable.card)
            }
        }
    }

    internal fun setCards(cards: List<Card>) {
        this.cards = cards
        notifyDataSetChanged()
    }

    override fun getItemCount() = cards.size
}