package nl.bryanderidder.byheart.card

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_card.view.*
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.shared.Preferences
import nl.bryanderidder.byheart.shared.get
import nl.bryanderidder.byheart.shared.getAttr
import nl.bryanderidder.byheart.shared.setBrightness


/**
 * Adapter that contains all cards in a pile.
 * @author Bryan de Ridder
 */
class CardListAdapter internal constructor(
    private val context: Context,
    val cardFragment: CardFragment
) : RecyclerView.Adapter<CardListAdapter.CardViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    var cards: MutableList<Card> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val itemView = inflater.inflate(R.layout.item_card, parent, false)
        return CardViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = cards.find { it.listIndex == position }
        holder.itemView.tvFront.text = card?.question
        holder.itemView.tvBack.text = card?.answer
        holder.itemView.tvCorrectPercentage.text = "${card?.getCorrectPercentage()}%"
        holder.itemView.cvCard.setOnClickListener {
            card?.let { cardFragment.startEditFragment(it.id) }
        }
        if (Preferences.DARK_MODE) holder.itemView.tvFront.setTextColor(cardFragment.pileColor)
        else holder.itemView.tvFront.setTextColor(cardFragment.pileColor.setBrightness(0.55F))
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Comparable<T>> sort(property: String, cards: MutableList<Card> = this.cards) {
        val newCards = cards.toMutableList()
        if (T::class != Int::class) newCards.sortBy { it[property] as T }
        else newCards.sortByDescending { it[property] as T }
        cards.sortBy { it.listIndex }
        newCards.forEachIndexed { j, newCard ->
            newCard.listIndex = j
            val i = cards.indexOfFirst { it.id == newCard.id }
            cards.add(j, cards.removeAt(i))
            notifyItemMoved(i, j)
        }
        cardFragment.updateReorderedCards(newCards)
    }

    internal fun setCards(cards: MutableList<Card>) {
        cards.sortBy { it.listIndex }
        val testCards = cards.filterIndexed { i, c -> c.listIndex == i }
        if (testCards.size != cards.size) sort<String>("question", cards)
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

    fun doAfterMovingPiles() = cardFragment.updateAllCards(cards)

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}