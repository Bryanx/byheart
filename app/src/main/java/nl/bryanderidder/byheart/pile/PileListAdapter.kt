package nl.bryanderidder.byheart.pile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
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
    private val cards: List<Card>
) : RecyclerView.Adapter<PileListAdapter.PileViewHolder>() {

    private var darkMode: Boolean = false
    private lateinit var sessionVM: SessionViewModel
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var piles = emptyList<Pile>() // Cached copy

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PileViewHolder {
        val itemView = inflater.inflate(R.layout.item_pile, parent, false)
        sessionVM = ViewModelProviders.of(context as MainActivity).get(SessionViewModel::class.java)
        darkMode = Preferences.DARK_MODE
        return PileViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PileViewHolder, position: Int) {
        val current = piles[position]
        val cardCount = cards.count { it.pileId == piles[position].id }
        val item = holder.itemView
        item.tvPileFront.text = current.name
        item.tvPileId.text = current.id.toString()
        item.tvCardsCount.text = context.resources.getString(R.string.card_count, cardCount)
        if (darkMode) {
            current.color?.let { item.tvPileFront.setTextColor(it) }
            item.cvPile.setCardBackgroundColor(context.getAttr(R.attr.mainBackgroundColorLighter))
        } else {
            current.color?.let {
                item.tvPileFront.setTextColor(it.setBrightness(0.55F))
            }
        }
    }

    internal fun setPiles(piles: List<Pile>) {
        this.piles = piles
        notifyDataSetChanged()
    }

    override fun getItemCount() = piles.size

    inner class PileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                sessionVM.pileId.postValue(itemView.tvPileId.long)
                sessionVM.pileName.postValue(itemView.tvPileFront.string)
                (it.context as MainActivity).supportFragmentManager.startFragment(CardFragment())
            }
        }
    }
}
