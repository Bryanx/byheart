package com.example.byheart.card

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.byheart.R
import com.example.byheart.shared.Preferences
import com.example.byheart.shared.Preferences.DARK_MODE
import com.example.byheart.shared.getAttr

/**
 * Adapter that contains all cards in a pile.
 * @author Bryan de Ridder
 */
class CardListAdapter internal constructor(
    private val context: Context,
    private val cardFragment: CardFragment
) :
    RecyclerView.Adapter<CardListAdapter.CardViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    var cards: MutableList<Card> = mutableListOf()

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardItemView: TextView = itemView.findViewById(R.id.tvFront)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val itemView = inflater.inflate(R.layout.item_card, parent, false)
        return CardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val current = cards[position]
        val itemView = holder.cardItemView
        changeCardColor(itemView)
        itemView.text = current.question
        itemView.setOnClickListener {
            if (itemView.currentTextColor != Color.WHITE) {
                itemView.text = current.answer
                itemView.setTextColor(Color.WHITE)
                itemView.setBackgroundResource(R.drawable.card_answer)
            } else {
                itemView.text = current.question
                changeCardColor(itemView)
            }
        }
    }

    private fun changeCardColor(itemView: TextView) {
        val color = context.getAttr(R.attr.mainTextColor)
        itemView.setTextColor(color)
        if (Preferences.read(DARK_MODE)) {
            itemView.setBackgroundResource(R.drawable.card_dark)
        } else {
            itemView.setBackgroundResource(R.drawable.card_light)
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

    fun editItem(i: Int): Unit = cardFragment.startEditFragment(this.cards[i].id)

    fun getContext(): Context = context

    override fun getItemCount(): Int = cards.size

}