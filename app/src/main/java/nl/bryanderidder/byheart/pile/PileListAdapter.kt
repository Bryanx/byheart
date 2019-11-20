package nl.bryanderidder.byheart.pile

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_pile.view.*
import nl.bryanderidder.byheart.MainActivity
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.card.Card
import nl.bryanderidder.byheart.card.CardFragment
import nl.bryanderidder.byheart.shared.*

/**
 * Adapter that contains all Piles in the main pile fragment (home page).
 * @author Bryan de Ridder
 */
class PileListAdapter internal constructor(
    private val context: Context,
    val cards: MutableList<Card>,
    var sessionVM: SessionViewModel,
    private var pileVM: PileViewModel,
    var fragment: PileFragment
) : RecyclerView.Adapter<PileListAdapter.PileViewHolder>() {

    private var darkMode: Boolean = false
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    var piles: MutableList<Pile> = mutableListOf() // Cached copy

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PileViewHolder {
        val itemView = inflater.inflate(R.layout.item_pile, parent, false)
        darkMode = Preferences.DARK_MODE
        return PileViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PileViewHolder, position: Int) {
        val pile = piles.find { it.listIndex == position }
        val cardCount = cards.count { it.pileId == pile?.id }
        val item = holder.itemView
        item.tvPileFront.text = pile?.name
        item.tvPileId.text = pile?.id.toString()
        item.tvCardsCount.text = "$cardCount ${context.resources.getString(R.string.cards)}"
        if (darkMode) {
            pile?.color?.let { item.tvPileFront.setTextColor(it) }
            item.cvPile.setCardBackgroundColor(context.getAttr(R.attr.mainBackgroundColorLighter))
        } else {
            pile?.color?.let {
                item.tvPileFront.setTextColor(it.setBrightness(0.55F))
            }
        }
    }

    internal fun setPiles(piles: MutableList<Pile>) {
        this.piles = piles
        notifyDataSetChanged()
    }

    override fun getItemCount() = piles.size

    fun doAfterMovingPiles() = pileVM.updateAll(piles)

    inner class PileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                sessionVM.pileId.postValue(itemView.tvPileId.long)
                sessionVM.pileName.postValue(itemView.tvPileFront.string)
                sessionVM.pileColor.postValue(piles.first { it.id == itemView.tvPileId.long }.color)
                (it.context as MainActivity).supportFragmentManager.startFragment(CardFragment())
            }
        }
    }
}
