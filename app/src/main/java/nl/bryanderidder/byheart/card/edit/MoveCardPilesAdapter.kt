package nl.bryanderidder.byheart.card.edit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_pile_move.view.*
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.pile.Pile
import nl.bryanderidder.byheart.shared.Preferences
import nl.bryanderidder.byheart.shared.getAttr
import nl.bryanderidder.byheart.shared.long
import nl.bryanderidder.byheart.shared.setBrightness

/**
 * Adapter that popups up in the MoveCard bottomsheet
 * @author Bryan de Ridder
 */
class MoveCardPilesAdapter(
    private val context: Context,
    private val onClickMoveCard: (pileId: Long) -> Unit
) :
    RecyclerView.Adapter<MoveCardPilesAdapter.PileViewHolder>() {

    var piles: List<Pile> = listOf()
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PileViewHolder {
        val itemView = inflater.inflate(R.layout.item_pile_move, parent, false)
        return PileViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PileViewHolder, position: Int) {
        val pile = piles[position]
        val item = holder.itemView
        item.cvCard.setCardBackgroundColor(context.getAttr(R.attr.dialogBackgroundButton))
        item.tvPileName.text = pile.name
        item.tvPileId.text = pile.id.toString()
        if (Preferences.DARK_MODE)
            pile.color?.let { item.tvPileName.setTextColor(it) }
        else
            pile.color?.let { item.tvPileName.setTextColor(it.setBrightness(0.55F)) }

    }

    override fun getItemCount(): Int = piles.size

    fun setList(list: List<Pile>) {
        piles = list
        notifyDataSetChanged()
    }

    inner class PileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                onClickMoveCard(it.tvPileId.long)
            }
        }
    }
}