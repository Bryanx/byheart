package com.example.byheart.card

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.byheart.R

class CardListAdapter internal constructor(
    private val context: Context,
    private val cardFragment: CardFragment
) :
    RecyclerView.Adapter<CardListAdapter.CardViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var cards: MutableList<Card> = mutableListOf()

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardItemView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_card_item, parent, false)
        return CardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val current = cards[position]
        val itemView = holder.cardItemView
        itemView.text = current.question
        itemView.setOnClickListener {
            if (itemView.text == current.question) {
                itemView.text = current.answer
                itemView.setTextColor(Color.WHITE)
                itemView.setBackgroundResource(R.drawable.card_answer)
            } else {
                itemView.text = current.question
                itemView.setTextColor(Color.BLACK)
                itemView.setBackgroundResource(R.drawable.card)
            }
        }
    }

    internal fun setCards(cards: List<Card>) {
        this.cards.clear()
        this.cards.addAll(cards)
        notifyDataSetChanged()
    }

    fun deleteItem(i: Int) {
        val card = this.cards.removeAt(i)
        cardFragment.removeCard(card)
        notifyItemRemoved(i)
    }

    fun getContext(): Context = context

    override fun getItemCount(): Int = cards.size

}