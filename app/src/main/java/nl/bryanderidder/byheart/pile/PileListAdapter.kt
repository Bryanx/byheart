package nl.bryanderidder.byheart.pile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import nl.bryanderidder.byheart.MainActivity
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.card.CardFragment
import nl.bryanderidder.byheart.shared.SessionViewModel
import nl.bryanderidder.byheart.shared.long
import nl.bryanderidder.byheart.shared.startFragment
import nl.bryanderidder.byheart.shared.string

/**
 * Adapter that contains all Piles in the main pile fragment (home page).
 * @author Bryan de Ridder
 */
class PileListAdapter internal constructor(context: Context) : RecyclerView.Adapter<PileListAdapter.PileViewHolder>() {

    private lateinit var sessionVM: SessionViewModel
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var piles = emptyList<Pile>() // Cached copy

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PileViewHolder {
        val itemView = inflater.inflate(R.layout.item_pile, parent, false)
        sessionVM = ViewModelProviders.of(parent.context as MainActivity).get(SessionViewModel::class.java)
        return PileViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PileViewHolder, position: Int) {
        val current = piles[position]
        holder.tvFront.text = current.name
        holder.tvPileId.text = current.id.toString()
    }

    internal fun setPiles(piles: List<Pile>) {
        this.piles = piles
        notifyDataSetChanged()
    }

    override fun getItemCount() = piles.size

    inner class PileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvFront: TextView = itemView.findViewById(R.id.tvPileFront)
        val tvPileId: TextView = itemView.findViewById(R.id.tvPileId)
        init {
            itemView.setOnClickListener {
                sessionVM.pileId.postValue(tvPileId.long)
                sessionVM.pileName.postValue(tvFront.string)
                (it.context as MainActivity).supportFragmentManager.startFragment(CardFragment())
            }
        }
    }
}
