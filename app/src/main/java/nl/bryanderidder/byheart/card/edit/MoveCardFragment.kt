package nl.bryanderidder.byheart.card.edit

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.NO_ID
import kotlinx.android.synthetic.main.content_card_move_bottomsheet.*
import kotlinx.android.synthetic.main.content_card_move_bottomsheet.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import nl.bryanderidder.byheart.BaseBottomSheet
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.card.Card
import nl.bryanderidder.byheart.card.CardFragment
import nl.bryanderidder.byheart.card.CardViewModel
import nl.bryanderidder.byheart.pile.PileViewModel
import nl.bryanderidder.byheart.shared.SessionViewModel
import nl.bryanderidder.byheart.shared.startFragment
import org.koin.android.viewmodel.ext.android.sharedViewModel

/**
 * Fragment that pops up to select a pile where to move the card to
 * @author Bryan de Ridder
 */
class MoveCardFragment : BaseBottomSheet() {

    private val pileVM: PileViewModel by sharedViewModel()
    private val sessionVM: SessionViewModel by sharedViewModel()
    private val cardVM: CardViewModel by sharedViewModel()
    private var moveCardJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.content_card_move_bottomsheet, container, false)
        setUpAdapter(layout)
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar.setCircleColor(sessionVM.pileColor.value!!)
    }

    private fun setUpAdapter(layout: View) {
        val moveCardPilesAdapter = MoveCardPilesAdapter(context!!, ::onClickMoveCard)
        layout.pileRecycler.adapter = moveCardPilesAdapter
        layout.pileRecycler.layoutManager = LinearLayoutManager(context!!)
        pileVM.allPiles.observe(this, Observer {
            moveCardPilesAdapter.setList(it
                .toMutableList()
                .filter { pile -> pile.id != sessionVM.pileId.value ?: NO_ID }
                .toList())
        })
    }

    private fun onClickMoveCard(pileId: Long) {
        showProgressBar(true)
        moveCardJob = lifecycleScope.launch {
            val cardId = sessionVM.cardId
            val card: Card? = cardVM.allCards.value?.find { it.id == cardId.value }
            card?.let {
                cardVM.deleteAsync(it).await()
                cardVM.insertAsync(Card(it.question, it.answer, pileId)).await()
            }
            activity?.runOnUiThread {
                sessionVM.message.value = getString(R.string.card_was_moved)
                showProgressBar(false)
                dismiss()
                startFragment(CardFragment())
            }
        }
    }

    private fun showProgressBar(show: Boolean) {
        pileRecycler.alpha = if (show) 0F else 1F
        crsTitle.alpha = if (show) 0F else 1F
        progressBar.alpha = if (show) 1F else 0F
    }

    override fun onDismiss(dialog: DialogInterface) {
        moveCardJob?.cancel()
        super.onDismiss(dialog)
    }
}