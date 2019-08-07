package nl.bryanderidder.byheart.card

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_card.view.*
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.shared.color
import nl.bryanderidder.byheart.shared.getAttr

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
    private var itemDeleted = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val itemView = inflater.inflate(R.layout.item_card, parent, false)
        return CardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val current = cards[position]
        val tvFront = holder.itemView.tvFront
        val cvCard = holder.itemView.cvCard
        tvFront.setTextColor(context.getAttr(R.attr.mainTextColor))
        tvFront.text = current.question
        tvFront.setOnClickListener {
            if (tvFront.currentTextColor != Color.WHITE) {
                tvFront.text = current.answer
                tvFront.setTextColor(Color.WHITE)
                cvCard.setCardBackgroundColor(context.color(R.color.colorPrimary))
            } else {
                tvFront.text = current.question
                tvFront.setTextColor(context.getAttr(R.attr.mainTextColor))
                cvCard.setCardBackgroundColor(context.getAttr(R.attr.mainBackgroundColorLighter))
            }
        }
    }

    internal fun setCards(cards: List<Card>) {
        if (itemDeleted) return
        this.cards.clear()
        this.cards.addAll(cards)
        notifyDataSetChanged()
    }

    fun deleteItem(i: Int) {
        itemDeleted = true
        val card = this.cards.removeAt(i)
        cardFragment.removeCard(card)
        notifyItemRemoved(i)
    }

    fun editItem(i: Int): Unit = cardFragment.startEditFragment(this.cards[i].id)

    fun getContext(): Context = context

    override fun getItemCount(): Int = cards.size

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}