package nl.bryanderidder.byheart.card

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_card.view.*
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.shared.getAttr


/**
 * Adapter that contains all cards in a pile.
 * @author Bryan de Ridder
 */
class CardListAdapter internal constructor(
    private val context: Context,
    val cardFragment: CardFragment
) :
    RecyclerView.Adapter<CardListAdapter.CardViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    var cards: MutableList<Card> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val itemView = inflater.inflate(R.layout.item_card, parent, false)
        return CardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = cards.find { it.listIndex == position }
        holder.itemView.tvFront.text = card?.question
        holder.itemView.tvBack.text = card?.answer
        holder.itemView.tvCorrectPercentage.text = "${card?.getCorrectPercentage()}%"
        holder.itemView.cvCard.setOnClickListener {
            card?.let { cardFragment.startEditFragment(it.id) }
        }
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Comparable<T>> sort(property: String) {
        val newCards = cards.toMutableList()
        if (T::class != Int::class) newCards.sortBy { it.getAttr(property) as T }
        else newCards.sortByDescending { it.getAttr(property) as T }
        cards.sortBy { it.listIndex }
        newCards.forEachIndexed { j, newCard ->
            newCard.listIndex = j
            val i = cards.indexOfFirst { it.id == newCard.id }
            cards.add(j, cards.removeAt(i))
            notifyItemMoved(i, j)
        }
        cardFragment.updateReorderedCards(newCards)
    }

    internal fun setCards(cards: List<Card>) {
        this.cards.clear()
        this.cards.addAll(cards)
        notifyDataSetChanged()
    }

    fun deleteItem(i: Int) {
        cards.find { it.listIndex == i }?.let {
            cardFragment.removeCard(it)
        }
    }

    fun editItem(i: Int): Unit = cardFragment.startEditFragment(this.cards[i].id)

    fun getContext(): Context = context

    override fun getItemCount(): Int = cards.size

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}