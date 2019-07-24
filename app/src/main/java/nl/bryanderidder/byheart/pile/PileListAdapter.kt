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
import nl.bryanderidder.byheart.card.CardFragment
import nl.bryanderidder.byheart.shared.*

/**
 * Adapter that contains all Piles in the main pile fragment (home page).
 * @author Bryan de Ridder
 */
class PileListAdapter internal constructor(private val context: Context) : RecyclerView.Adapter<PileListAdapter.PileViewHolder>() {

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
        val color = context.color(current.color!!)
        holder.itemView.tvPileFront.text = current.name
        holder.itemView.tvPileId.text = current.id.toString()
        if (darkMode) {
            holder.itemView.tvPileFront.setTextColor(color)
            holder.itemView.ivPile.setDrawableColor(R.drawable.ic_card_no_shadow, context.getAttr(R.attr.mainBackgroundColorLighter))
        } else {
            holder.itemView.tvPileFront.setTextColor(color.setBrightness(0.15F))
            holder.itemView.ivPile.setDrawableColor(R.drawable.ic_card_no_shadow, color)
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
